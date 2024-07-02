package com.example.springboot;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
public class HelloController {

	//static list of possible weather statuses
    private static final String[] WEATHER_CONDITIONS = {
        "Sunny", "Cloudy", "Rainy", "Snowy", "Overcast", "Drizzles", "Fine", "Stormy", "Windy", "Foggy", "Hazy", "Misty", "Showers", "Thunderstorms", "Tornado", "Hurricane", "Blizzard", "Sleet", "Hail", "Duststorm", "Sandstorm", "Heatwave", "Coldwave", "Tsunami", "Earthquake", "Volcanic Eruption", "Asteroid Impact", "Alien Invasion", "Zombie Apocalypse", "Nuclear Winter", "Post-Apocalyptic", "Interstellar", "Intergalactic", "Interspatial", "Intertemporal", "Interdimensional", "Infinite", "Unknown"
    };

    private ExecutorService executor = Executors.newCachedThreadPool();

	@GetMapping("/")
	public String index() {
		return "Greetings from Spring Boot!";
	}

	@GetMapping("/rbe")
    public SseEmitter handleRbe() {
        SseEmitter emitter = new SseEmitter();
    
        executor.execute(() -> {
            //emit Weather Status for each WEATHER_CONDITIONs once every second until all are emitted
            for (int i = 0; i < WEATHER_CONDITIONS.length; i++) {
                //WeatherStatus weatherStatus = new WeatherStatus(WEATHER_CONDITIONS[i], Math.random() * 100, LocalDateTime.now());
                try {
                    emitter.send(
                        SseEmitter.event().name("weather-status").data(WEATHER_CONDITIONS[i]).reconnectTime(1000)
                    );
                    System.out.println("Weather Status: " + WEATHER_CONDITIONS[i]);
                    Thread.sleep(500);
                } catch (Exception e) {
                    emitter.completeWithError(e);
                    break;
                }
            }
            emitter.complete();
        });
        return emitter;
    }
}
