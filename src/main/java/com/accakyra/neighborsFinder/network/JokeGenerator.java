package main.java.com.accakyra.neighborsFinder.network;

import java.util.ArrayList;
import java.util.List;

public class JokeGenerator {

    private List<String> jokes;
    private int expiredJokeAmount;

    public JokeGenerator() {
        expiredJokeAmount = 0;
        jokes = new ArrayList<>();
        jokes.add("Chuck Norris' first job was as a paperboy. There were no survivors.");
        jokes.add("SON: What are clouds made of? DAD: Linux servers, mostly");
        jokes.add("When Chuck Norris goes to out to eat, he orders a whole chicken, but he only eats its soul.");
        jokes.add("Chuck Norris does not need to type-cast. The Chuck-Norris Compiler (CNC) sees through things. All way down. Always.");
        jokes.add("Rules of fighting: 1) Don't bring a knife to a gun fight. 2) Don't bring a gun to a Chuck Norris fight.");
        jokes.add("Chuck Norris won super bowls VII and VIII singlehandedly before unexpectedly retiring to pursue a career in ass-kicking.");
        jokes.add("In honor of Chuck Norris, all McDonald's in Texas have an even larger size than the super-size. When ordering, just ask to be Chucksized.");
    }

    public String getJoke() {
        if (expiredJokeAmount == jokes.size()) {
            expiredJokeAmount = 0;
        }
        int jokeNumber = (int) (Math.random() * (jokes.size() - expiredJokeAmount));
        String joke = jokes.get(jokeNumber);
        jokes.add(jokes.remove(jokeNumber));
        expiredJokeAmount++;
        return joke;
    }
}
