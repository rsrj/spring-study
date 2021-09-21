package com.rsrj.devdojo.springboot2.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

/*O component eh necessario para que possa haver a injecao de dependencias
 * com o Autowired*/
@Component
public class DateUtil {
	public String formatLocalDateTimeToDatabaseStyle(LocalDateTime localDateTime){
		return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(localDateTime);
	}
}
