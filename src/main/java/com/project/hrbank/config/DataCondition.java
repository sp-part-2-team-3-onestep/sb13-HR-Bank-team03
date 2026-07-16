package com.project.hrbank.config;


import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class DataCondition {
    private boolean DataChanged = false;

    public boolean checkDataChanged(){
        return DataChanged;
    }

    public void flagSetChanged(){
        DataChanged = true;
    }

    public void flagSetUnchanged(){
        DataChanged = false;
    }

}
