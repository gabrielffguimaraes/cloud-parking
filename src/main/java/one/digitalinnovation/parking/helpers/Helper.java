package one.digitalinnovation.parking.helpers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class Helper {
    @Autowired
    private ModelMapper modelMapper;

    public  <S,T> List<S> mapList(List<T> elementList, Class<S> targetClass) {

        return elementList
                .stream()
                .map(element -> modelMapper.map(element, targetClass))
                .collect(Collectors.toList());
    }
}
