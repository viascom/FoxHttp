package ch.viascom.groundwork.foxhttp.interceptor;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

/**
 * @author patrick.boesch@viascom.ch
 */
public class DefaultInterceptorStrategy implements FoxHttpInterceptorStrategy {

    @Getter
    @Setter
    private Map<FoxHttpInterceptorType, HashMap<String, FoxHttpInterceptor>> foxHttpInterceptors = new EnumMap<>(FoxHttpInterceptorType.class);


    @Override
    public void addInterceptor(FoxHttpInterceptorType type, FoxHttpInterceptor interceptor) {
        addInterceptor(type, interceptor, String.valueOf(UUID.randomUUID()));
    }

    public void addInterceptor(FoxHttpInterceptorType type, FoxHttpInterceptor interceptor, String key) {
        if (foxHttpInterceptors.containsKey(type)) {
            foxHttpInterceptors.get(type).put(key, interceptor);
        } else {
            HashMap<String, FoxHttpInterceptor> foxHttpInterceptorMap = new HashMap<>();
            foxHttpInterceptorMap.put(key, interceptor);
            foxHttpInterceptors.put(type, foxHttpInterceptorMap);
        }
    }

    @Override
    public void removeInterceptorByKey(FoxHttpInterceptorType type, String key) {
        HashMap<String, FoxHttpInterceptor> clearedMap = new HashMap<>();
        foxHttpInterceptors.get(type).entrySet()
                .stream()
                .filter(entry -> !entry.getKey().equals(key))
                .forEach(interceptorEntry -> clearedMap.put(interceptorEntry.getKey(), interceptorEntry.getValue()));

        foxHttpInterceptors.get(type).clear();
        foxHttpInterceptors.get(type).putAll(clearedMap);
    }

    @Override
    public void removeInterceptorByClass(FoxHttpInterceptorType type, Class<FoxHttpInterceptor> clazz) {
        HashMap<String, FoxHttpInterceptor> clearedMap = new HashMap<>();
        foxHttpInterceptors.get(type).entrySet()
                .stream()
                .filter(entry -> !entry.getValue().getClass().isAssignableFrom(clazz))
                .forEach(interceptorEntry -> clearedMap.put(interceptorEntry.getKey(), interceptorEntry.getValue()));

        foxHttpInterceptors.get(type).clear();
        foxHttpInterceptors.get(type).putAll(clearedMap);
    }

    @Override
    public void replaceInterceptor(FoxHttpInterceptorType type, FoxHttpInterceptor newInterceptor, String key) {
        removeInterceptorByKey(type, key);
        foxHttpInterceptors.get(type).put(key, newInterceptor);
    }

    @Override
    public FoxHttpInterceptor getInterceptorByKey(FoxHttpInterceptorType type, String key) {
        return foxHttpInterceptors.get(type).get(key);
    }

    @Override
    public HashMap<String, FoxHttpInterceptor> getAllInterceptorsFromType(FoxHttpInterceptorType type) {
        return foxHttpInterceptors.get(type);
    }

    @Override
    public ArrayList<FoxHttpInterceptor> getAllInterceptorsFromTypeAsArray(FoxHttpInterceptorType type, boolean sorted) {
        ArrayList<FoxHttpInterceptor> innerInterceptorList = new ArrayList<>();

        foxHttpInterceptors.get(type).forEach((key, value) -> innerInterceptorList.add(value));
        if (sorted) {
            innerInterceptorList.sort(new FoxHttpInterceptorComparator());
        }
        return innerInterceptorList;
    }

}
