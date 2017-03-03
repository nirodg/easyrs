package com.dorinbrage.easyrs.processor;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

import org.apache.commons.lang.ClassUtils;

import com.dorinbrage.easyrs.data.DtoData;
import com.dorinbrage.easyrs.processor.common.AnnotatedClass;
import com.dorinbrage.easyrs.processor.common.CommonProcessor;
import com.dorinbrage.easyrs.processor.common.Processor;
import com.dorinbrage.easyrs.processor.common.Utils;
import com.google.auto.service.AutoService;
import com.google.gson.Gson;

/**
 * The Processor
 * 
 * @author Dorin Brage
 */
@AutoService(Processor.class)
public class JsonProcessor extends CommonProcessor<DtoData> implements Processor {

  private Gson gson;

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

    gson = new Gson();
    this.roundEnv = roundEnv;
    getAnnotatedInterfaces();
    
    for (Entry<String, AnnotatedClass> item : container.entrySet()) {

      Class<?> clazzDto = null;
      Object objDto = null;
      DtoData data = new DtoData();

      try {
        clazzDto = Class.forName(item.getValue().getEntity().toString());
        objDto = clazzDto.newInstance();
        Utils.initializeFields(clazzDto, objDto);

        data.setGetAll(0);
        data.setCreate(objDto);
        data.setUpdate(objDto);

      } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
        e.printStackTrace();
      }


      System.out.println("Generating JSON file for " + item.getValue().getEndpoint().toString());

      String filePath =
          "C:\\Users\\Dorin_Brage\\Documents\\easyrs-example\\src\\test\\resources\\com\\dorinbrage\\github\\jme\\note\\dto\\"
              + ClassUtils.getShortClassName(item.getValue().getEndpoint().toString()).concat(".json");

      try (FileWriter fw = new FileWriter(filePath)) {

        System.out.println(filePath);
        gson.toJson(data, fw);

      } catch (IOException e) {
        e.printStackTrace();
      }


    }

    return true;
  }

}
