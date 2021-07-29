package scenarios.cli

// default-my-var
cliEnv.my_var = "webtau"
// default-my-var

cliEnv.prefix_var = "__"


// personas-my-var
personas {
    Alice {
//        cliEnv = [my_var:  "Alice!"]
        cliEnv.my_var = "Alice!"
    }

    Bob {
        cliEnv = [my_var:  "Bob!"]
//        cliEnv.my_var = "Bob!"
    }
}
// personas-my-var
