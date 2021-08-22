package scenarios.cli

// default-my-var
cliEnv {
    MY_VAR = "webtau"
    PREFIX_VAR = "__"
}
// default-my-var

// personas-my-var
personas {
    Alice {
        cliEnv.MY_VAR = "Alice!"
    }

    Bob {
        cliEnv.MY_VAR = "Bob!"
    }
}
// personas-my-var
