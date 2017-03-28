package provider;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Joachim Brehmer on 28.03.2017.
 */
public abstract class Observable<T> implements IObservable<T> {
    private List<IObserver<T>> observers = new CopyOnWriteArrayList<>();

    public void addObserver(IObserver<T> observer){
        observers.add(observer);
    }

    public boolean removeObserver(IObserver<T> observer){
        return observers.remove(observer);
    }

    @Override
    public void notifyObservers(T argument) {
        for (IObserver<T> observer : observers) {
            observer.update(this,argument);
        }
    }
}
