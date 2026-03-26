package service;

import com.google.gson.Gson;

import java.util.ArrayList;

public record ListGamesResult (ArrayList<GameSummary> games) {
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
