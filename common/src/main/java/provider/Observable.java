package provider;

import java.util.Set;

/**
 * Created by Joachim Brehmer on 28.03.2017.
 */
public class Observable<T> implements IObservable<T> {
    private Set<IObserver<T>> observers = new java.util.concurrent.CopyOnWriteArraySet<>();

    public void addObserver(IObserver<T> observer){
        observers.add(observer);
    }

    public boolean removeObserver(IObserver<T> observer){
        return observers.remove(observer);
    }

    public void notifyObservers(T argument) {
        for (IObserver<T> observer : observers) {
            observer.update(this,argument);
        }
    }
}
