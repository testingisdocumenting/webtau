package com.example.tests.junit5;

import org.testingisdocumenting.webtau.persona.Persona;

import java.util.Collections;

public class Personas {
    public static final Persona Alice = Persona.persona("Alice",
            Collections.singletonMap("authId", "alice-user-id"));
    public static final Persona Bob = Persona.persona("Bob",
            Collections.singletonMap("authId", "bob-user-id"));
}
