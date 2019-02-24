package apputil;

import java.util.function.Function;

public class Throwables {

    private Throwables(){
    }

    public static <T, K> Function<T, K> propagate(CheckedFunction<T, K> throwingFunction){
            return o -> {
                try{
                    return throwingFunction.apply(o);
                } catch (Throwable ex){
                    throw new RuntimeException(ex);
                }
            };
    }


    public interface CheckedFunction<T, K> {
        K apply(T t) throws Throwable;
    }
}
