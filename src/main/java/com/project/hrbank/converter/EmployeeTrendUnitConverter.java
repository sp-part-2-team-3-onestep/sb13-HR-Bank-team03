package com.project.hrbank.converter;

import com.project.hrbank.domain.EmployeeTrendUnit;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class EmployeeTrendUnitConverter
    implements Converter<String, EmployeeTrendUnit> {

  @Override
  public EmployeeTrendUnit convert(String source) {

    return EmployeeTrendUnit.valueOf(
        source.toUpperCase()
    );
  }
}