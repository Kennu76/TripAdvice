package com.example.kennu76.tripadvice.services;

import com.example.kennu76.tripadvice.domain.Location;

import java.util.ArrayList;
import java.util.List;

public class SkyscannerService {
    String apiKey = new EnvironmentService().apiKey;


    private List<Location> getLocations(String destination, String cost) {
        List<Location> locationList = new ArrayList<>();
        String url = "http://partners.api.skyscanner.net/apiservices/browseroutes/v1.0/US" +
                "/EUR/en-GB/" + destination + "/anywhere/anytime/anytime?apiKey=" + apiKey;
        return locationList;
    }
}
