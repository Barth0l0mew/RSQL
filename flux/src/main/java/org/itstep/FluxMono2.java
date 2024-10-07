package org.itstep;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class FluxMono2 {
    public static void main(String[] args) {
        List<Integer> elements = new ArrayList<>();

//
//        Flux.just(1, 2, 3, 4)
//                .log()
//                .subscribe(elements::add);
//        elements.stream().forEach(System.out::println);
//        System.out.println("flux 1,2,3,4===== ");
     /*   //Subscriber
        Flux.just(1, 2, 3, 4)
                .log()
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                      //  s.request(Long.MAX_VALUE);

s.request(2);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        elements.add(integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
        elements.stream().forEach(System.out::println);
        */
/*
        //Backpressure
        Flux.just(1, 2, 3, 4)
                .log()
                .subscribe(new Subscriber<Integer>() {
                    private Subscription s;
                    int onNextAmount;

                    @Override
                    public void onSubscribe(Subscription s) {
                        this.s = s;
                        s.request(2);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        elements.add(integer);
                        onNextAmount++;
                        if (onNextAmount % 2 == 0) {
                            s.request(2);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
        elements.stream().forEach(System.out::println);
*/

/*
        //zip
        List<String> elemStrings = new ArrayList<>();
        Flux.just(1, 2, 3, 4)
                .log()
                .map(i -> i * 2)
                .zipWith(Flux.range(0, Integer.MAX_VALUE),
                        (one, two) -> String.format("First Flux: %d, Second Flux: %d", one, two))
                .subscribe(elemStrings::add);
        System.out.println(elemStrings);
*/
        //ConnectableFlux (Infinite)
        ConnectableFlux<Object> publish = Flux.create(sink -> {
            while (true) {

sink.next
(System.currentTimeMillis());
            }
        })
                .publish();
        publish.subscribe(System.out::println);
        publish.connect();



        ConnectableFlux<Object> publish2 = Flux.create(fluxSink -> {
            while(true) {

                fluxSink.next(System.currentTimeMillis());
            }
        })
                .delayElements(Duration.ofMillis(500))
                //.sample(ofSeconds(2))
                .publish();
                    publish2.subscribe(System.out::println);
        publish2.connect();



        //Concurrency
//        Flux.just(1, 2, 3, 4)
//                .log()
//                .map(i -> i * 2)
//                .subscribeOn(Schedulers.parallel())
//                .subscribe(elements::add);
    }
}
