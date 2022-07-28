package one.digitalinnovation.parking.helpers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Helper {

    private final ModelMapper modelMapper;

    public Helper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public  <S,T> List<S> mapList(List<T> elementList, Class<S> targetClass) {

        return elementList
                .stream()
                .map(element -> modelMapper.map(element, targetClass))
                .collect(Collectors.toList());
    }
    public <S, T> S  map(T element, Class<S> targetClass) {
        return modelMapper.map(element, targetClass);
    }

}
