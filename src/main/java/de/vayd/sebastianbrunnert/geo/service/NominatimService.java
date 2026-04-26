package de.vayd.sebastianbrunnert.geo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.vayd.sebastianbrunnert.geo.model.Location;
import de.vayd.sebastianbrunnert.geo.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@Service
public class NominatimService {

    private static final String NOMINATIM_BASE = "https://nominatim.openstreetmap.org";
    private static final String USER_AGENT = "VAYD-Backend/1.0 (contact@vayd.de)";

    @Autowired
    private LocationRepository locationRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    /**
     * Resolves a free-text location query to a Location entity with a fully populated
     * parent hierarchy (city → district → state → country). Deduplicates using the
     * OpenStreetMap ID (e.g. "R62611") as the stable identifier.
     *
     * @return the most specific Location, or null if nothing was found
     */
    public Location resolveAndSave(String query) throws Exception {
        JsonNode results = search(query);
        if (results.isEmpty()) return null;

        JsonNode primary = results.get(0);
        JsonNode address = primary.get("address");

        String countryName = textOrNull(address, "country");
        String stateName   = textOrNull(address, "state");
        String countyName  = textOrNull(address, "county");
        String cityName    = cityName(address);

        // Save hierarchy top-down so each child can reference its parent
        Location country = countryName != null ? findOrSave(countryName, null)                                                                  : null;
        Location state   = stateName   != null ? findOrSave(stateName + ", " + countryName, country)                                            : null;
        Location county  = countyName  != null ? findOrSave(countyName + ", " + stateName + ", " + countryName, state != null ? state : country) : null;

        // The primary result IS the queried location – use its OSM ID directly
        if (cityName == null) {
            // The query itself resolved to a state/country level – return deepest parent
            return county != null ? county : state != null ? state : country;
        }

        String id = osmId(primary);
        Location parent = county != null ? county : state != null ? state : country;
        return locationRepository.findById(id).orElseGet(() ->
                locationRepository.save(new Location().setId(id).setName(cityName).setParent(parent))
        );
    }

    // -------------------------------------------------------------------------

    private Location findOrSave(String query, Location parent) throws Exception {
        Thread.sleep(1000); // Nominatim policy: max 1 req/s
        JsonNode results = search(query);
        if (results.isEmpty()) return null;

        JsonNode result = results.get(0);
        String id   = osmId(result);
        String name = result.get("display_name").asText().split(",")[0].trim();

        return locationRepository.findById(id).orElseGet(() ->
                locationRepository.save(new Location().setId(id).setName(name).setParent(parent))
        );
    }

    private JsonNode search(String query) throws Exception {
        String url = NOMINATIM_BASE + "/search?q=" + URLEncoder.encode(query, StandardCharsets.UTF_8)
                + "&format=json&addressdetails=1&limit=1";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", USER_AGENT)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return objectMapper.readTree(response.body());
    }

    /** Converts Nominatim osm_type + osm_id to a stable string ID, e.g. "R62611". */
    private String osmId(JsonNode result) {
        String type = result.get("osm_type").asText();
        long   id   = result.get("osm_id").asLong();
        String prefix = switch (type) {
            case "node"     -> "N";
            case "way"      -> "W";
            case "relation" -> "R";
            default         -> "X";
        };
        return prefix + id;
    }

    /** Returns the most specific city-level name from an address node, or null if absent. */
    private String cityName(JsonNode address) {
        for (String key : new String[]{"city", "town", "village", "municipality", "suburb", "quarter"}) {
            String v = textOrNull(address, key);
            if (v != null) return v;
        }
        return null;
    }

    private String textOrNull(JsonNode node, String field) {
        return (node != null && node.has(field)) ? node.get(field).asText() : null;
    }
}
