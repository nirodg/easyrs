package com.dorinbrage.easyrs.processor;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.element.TypeElement;

import org.apache.commons.lang.ClassUtils;

import com.dorinbrage.easyrs.data.DtoData;
import com.dorinbrage.easyrs.processor.common.AnnotatedClass;
import com.dorinbrage.easyrs.processor.common.CommonProcessor;
import com.dorinbrage.easyrs.processor.common.Utils;
import com.dorinbrage.easyrs.processor.enums.ProcessorOptions;
import com.dorinbrage.easyrs.processor.exception.ProcessingException;
import com.google.auto.service.AutoService;
import com.google.gson.Gson;

/**
 * The JsonProcessor creates for each annotated class with {@code EndpointTest} a JSON file with all
 * the declared fields for the given DTO class
 * 
 * @see EndpointTest
 * 
 * @author Dorin Brage
 */
@AutoService(Processor.class)
@SupportedOptions({ProcessorOptions.RESOURCE_FOLDER})
public class JsonProcessor extends CommonProcessor {

  private static final String FILE_JSON_EXTENSIOn = ".json";
  private Gson gson;

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

    gson = new Gson();
    this.roundEnv = roundEnv;
    getAnnotatedInterfaces();

    for (Entry<String, AnnotatedClass> item : container.entrySet()) {
      String pathJsonFile;

      if (!processedInterfaces.contains(item.getValue())) {

        try {
          DtoData data = new DtoData();

          initializeObject(item, data);

          pathJsonFile = Utils.getPathForResource(processingEnv, ProcessorOptions.RESOURCE_FOLDER,
              item.getValue().getEntity().toString());

          pathJsonFile = pathJsonFile
              .concat(ClassUtils.getShortClassName(item.getValue().getEndpoint().toString()))
              .concat(FILE_JSON_EXTENSIOn);

          try (FileWriter fw = new FileWriter(pathJsonFile)) {
            gson.toJson(data, fw);

          } catch (IOException e) {
            e.printStackTrace();
          }

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
          e.printStackTrace();
        } catch (ProcessingException e) {
          e.printStackTrace();
        }

        // add processed interface to the list
        processedInterfaces.add(item.getValue());

      }

    }

    return true;
  }

  /**
   * Initializes the object
   * 
   * @param item the annotated class
   * @param data the data object
   * @throws ClassNotFoundException if the class was not found
   * @throws InstantiationException when instanciating the object
   * @throws IllegalAccessException if this Field object is enforcing Java language access control
   *         and the underlying field is either inaccessible or final.
   */
  private void initializeObject(Entry<String, AnnotatedClass> item, DtoData data)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException {
    Class<?> clazzDto;
    Object objDto;
    clazzDto = Class.forName(item.getValue().getEntity().toString());
    objDto = clazzDto.newInstance();
    Utils.initializeFields(clazzDto, objDto);

    data.setCreate(objDto);
    data.setUpdate(objDto);
  }

}
