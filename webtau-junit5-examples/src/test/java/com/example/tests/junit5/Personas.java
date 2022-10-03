package com.example.tests.junit5;

import org.testingisdocumenting.webtau.persona.Persona;

public class Personas {
    public static final Persona Alice = Persona.persona("Alice",
            "authId", "alice-user-id");
    public static final Persona Bob = Persona.persona("Bob",
            "authId", "bob-user-id");
}
