package provider;

/**
 * Created by Joachim Brehmer on 28.03.2017.
 */
public interface IObservable<T> {
    void addObserver(IObserver<T> observer);
    boolean removeObserver(IObserver<T> observer);

    void notifyObservers(T argument);
}
