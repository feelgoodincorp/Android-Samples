package com.example.user.statemachinetrying;

class Model implements Contract.Model {

    private Model(){}

    private static Model model = new Model();

    public static Model getModel() {
        return model;
    }
}
