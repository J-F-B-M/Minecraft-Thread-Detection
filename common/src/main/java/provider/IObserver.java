package provider;

/**
 * Created by Joachim Brehmer on 28.03.2017.
 */
public interface IObserver<T> {
    void update(IObservable<T> observable, T argument);
}
