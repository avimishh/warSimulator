package Observer;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;

public class Observable {
    private final int MAX_NUM_OF_THREADS = 2;

    private List<Observer> subscribers = Collections.synchronizedList(new ArrayList<>());
    private ExecutorService executor = Executors.newFixedThreadPool(MAX_NUM_OF_THREADS);

    protected void publish(Function<Observer, Void> function){
        for(Observer subscriber : subscribers){
            executor.execute(() -> function.apply(subscriber));
        }
    }

    public void subscribe(Observer subscriber){
        subscribers.add(subscriber);
    }

    public void unsubscribe(Observer subscriber){
        subscribers.remove(subscriber);
    }
}
