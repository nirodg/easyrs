package com.dbrage.lib.easyrs.processor;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic.Kind;

import com.dbrage.lib.easyrs.processor.annotation.EndpointTest;
import com.dbrage.lib.easyrs.processor.annotation.common.AnnotatedClass;
import com.dbrage.lib.easyrs.processor.builder.ClassBuilder;
import com.google.auto.service.AutoService;

/**
 * The Processor
 * 
 * @author Dorin_Brage
 */
@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class EasyRsProcessor extends AbstractProcessor {

	private Types typeUtils;
	private Elements elements;
	private Filer filer;
	private Messager messager;

	/** Will contain all the classes annotated with EndpointTest */
	private Map<String, AnnotatedClass> container;

	/** The Generator factory */
	private ClassBuilder generator;

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

		// Iterate over each class
		for (Element element : roundEnv.getElementsAnnotatedWith(EndpointTest.class)) {

			// Looks for interfaces with IndependentTest.class
			isOnlyInterfaceAnnotated(element);

			// The class with the annotation to be tested
			TypeElement annotatedClazz = (TypeElement) element;

			// It checks to do not generate the class twice
			if (!container.containsKey(annotatedClazz.getSimpleName().toString())) {
				System.err.println("New class found with EASYRS Annotation");

				container.put(annotatedClazz.getSimpleName().toString(),
						new AnnotatedClass(annotatedClazz, element.getAnnotation(EndpointTest.class)));

			} else {
				error(element, "%s it can't be generated twice", annotatedClazz.getSimpleName().toString());
				return true;
			}

			try {
				generator
						.init(annotatedClazz, container.get(annotatedClazz.getSimpleName().toString()), filer, elements)
						.build();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		return true;
	}

	@Override
	public synchronized void init(ProcessingEnvironment processingEnv) {
		super.init(processingEnv);
		typeUtils = processingEnv.getTypeUtils();
		elements = processingEnv.getElementUtils();
		filer = processingEnv.getFiler();
		messager = processingEnv.getMessager();

		container = new HashMap<String, AnnotatedClass>();
		generator = new ClassBuilder();
	}

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		Set<String> annotations = new LinkedHashSet<String>();
		annotations.add(EndpointTest.class.getCanonicalName());
		return annotations;
	}

	/**
	 * Checks if the annotation was used only by interfaces
	 * 
	 * @param element
	 *            Represents a program element such as a package, class, or
	 *            method
	 * @return {@code true} if the element is an {@code Interface},
	 *         {@code false} is it's not
	 */
	private boolean isOnlyInterfaceAnnotated(Element element) {
		if (element.getKind().equals(ElementKind.INTERFACE)) {
			return true;
		}
		error(element, "Only interfaces can be annotated with @%s", EndpointTest.class.getName());
		return false;
	}

	/**
	 * If an error occurs is shown during the process
	 * 
	 * @param element
	 *            Represents a program element such as a package, class, or
	 *            method
	 * @param string
	 *            The formated message
	 * @param args
	 *            Arguments referenced by the format specifiers in the format
	 *            string
	 */
	private void error(Element element, String string, Object... args) {
		messager.printMessage(Kind.ERROR, String.format(string, args), element);
		System.err.println(String.format(string, args));
	}
}
