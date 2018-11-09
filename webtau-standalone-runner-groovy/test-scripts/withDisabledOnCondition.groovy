onlyWhen('will never happen', { -> false}) {
    scenario('conditioned scenario one') {
        throw new RuntimeException('error that should not be thrown')
    }

    sscenario('conditioned scenario two') {
        throw new RuntimeException('error that should not be thrown')
    }
}

scenario('scenario three') {
}