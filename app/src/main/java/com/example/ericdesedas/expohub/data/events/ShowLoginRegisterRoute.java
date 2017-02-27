package com.example.ericdesedas.expohub.data.events;

public class ShowLoginRegisterRoute {
    public String method;
    public String route;

    public ShowLoginRegisterRoute(String method, String route) {
        this.route = route;
        this.method = method;
    }
}
