package ch.viascom.groundwork.foxhttp.interceptor;

import ch.viascom.groundwork.foxhttp.exception.FoxHttpException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;

/**
 * @author patrick.boesch@viascom.ch
 */
public class DefaultInterceptorStrategy implements FoxHttpInterceptorStrategy {

    @Getter
    @Setter
    private Map<FoxHttpInterceptorType, HashMap<String, FoxHttpInterceptor>> foxHttpInterceptors = new EnumMap<>(FoxHttpInterceptorType.class);


    @Override
    public void addInterceptor(FoxHttpInterceptorType type, FoxHttpInterceptor interceptor) throws FoxHttpException {
        addInterceptor(type, interceptor, String.valueOf(UUID.randomUUID()));
    }

    public void addInterceptor(FoxHttpInterceptorType type, FoxHttpInterceptor interceptor, String key) throws FoxHttpException {
        FoxHttpInterceptorType.verifyInterceptor(type, interceptor);
        if (doesTypeExist(type)) {
            foxHttpInterceptors.get(type).put(key, interceptor);
        } else {
            HashMap<String, FoxHttpInterceptor> foxHttpInterceptorMap = new HashMap<>();
            foxHttpInterceptorMap.put(key, interceptor);
            foxHttpInterceptors.put(type, foxHttpInterceptorMap);
        }
    }

    @Override
    public void removeInterceptorByKey(FoxHttpInterceptorType type, String key) {
        if (doesTypeExist(type)) {
            foxHttpInterceptors.put(type, new HashMap<>(
                foxHttpInterceptors.get(type).entrySet().stream().filter(entry -> !entry.getKey().equals(key)).collect(Collectors.toMap(Entry::getKey, Entry::getValue))));
        }
    }

    @Override
    public void removeInterceptorByClass(FoxHttpInterceptorType type, Class<? extends FoxHttpInterceptor> clazz) {
        if (doesTypeExist(type)) {
            foxHttpInterceptors.put(type, new HashMap<>(foxHttpInterceptors.get(type)
                                                                           .entrySet()
                                                                           .stream()
                                                                           .filter(entry -> !entry.getValue().getClass().isAssignableFrom(clazz))
                                                                           .collect(Collectors.toMap(Entry::getKey, Entry::getValue))));
        }
    }

    @Override
    public void replaceInterceptor(FoxHttpInterceptorType type, FoxHttpInterceptor newInterceptor, String key) {
        removeInterceptorByKey(type, key);
        if (doesTypeExist(type)) {
            foxHttpInterceptors.get(type).put(key, newInterceptor);
        }
    }

    @Override
    public FoxHttpInterceptor getInterceptorByKey(FoxHttpInterceptorType type, String key) {
        if (doesTypeExist(type)) {
            return foxHttpInterceptors.get(type).get(key);
        } else {
            return null;
        }
    }

    @Override
    public ArrayList<FoxHttpInterceptor> getInterceptorsByClass(FoxHttpInterceptorType type, Class<? extends FoxHttpInterceptor> clazz) {
        ArrayList<FoxHttpInterceptor> interceptorList = new ArrayList<>();
        if (doesTypeExist(type)) {
            interceptorList.addAll(foxHttpInterceptors.get(type)
                                                      .entrySet()
                                                      .stream()
                                                      .filter((Map.Entry<String, FoxHttpInterceptor> interceptor) -> interceptor.getValue().getClass().isAssignableFrom(clazz))
                                                      .map(Map.Entry::getValue)
                                                      .collect(Collectors.toList()));
        }
        return interceptorList;
    }

    @Override
    public HashMap<String, FoxHttpInterceptor> getAllInterceptorsFromType(FoxHttpInterceptorType type) {
        return foxHttpInterceptors.get(type);
    }

    @Override
    public ArrayList<FoxHttpInterceptor> getAllInterceptorsFromTypeAsArray(FoxHttpInterceptorType type, boolean sorted) {
        ArrayList<FoxHttpInterceptor> innerInterceptorList = new ArrayList<>();

        if (doesTypeExist(type)) {
            innerInterceptorList.addAll(foxHttpInterceptors.get(type).entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList()));
            if (sorted) {
                innerInterceptorList.sort(new FoxHttpInterceptorComparator());
            }
        }
        return innerInterceptorList;
    }

    @Override
    public boolean doesTypeExist(FoxHttpInterceptorType type) {
        return foxHttpInterceptors.containsKey(type);
    }

}
