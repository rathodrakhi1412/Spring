package example.web.example;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import example.domain.example.ExampleEntity;
import example.domain.example.ExampleRepository;
import example.domain.example.ExampleValueObject;
import example.web.AbstractContollerTest;
import example.web.RequestMappings;

public class ExampleControllerTest extends AbstractContollerTest {

	private static final String ANY_ENTITY_NAME = "any entity name";

	private static final String ANY_FIELD_A = "any field A";

	private static final String ANY_FIELD_B = "any field B";

	@Autowired
	private ExampleRepository exampleRepository;

	private ExampleEntity.Builder exampleEntityBuilder;

	private ExampleValueObject.Builder exampleValueBuilder;

	@BeforeMethod
	public void prepareBuilders() {
		exampleEntityBuilder = new ExampleEntity.Builder();
		exampleValueBuilder = new ExampleValueObject.Builder();

		exampleValueBuilder.withFieldA(ANY_FIELD_A).withFieldB(ANY_FIELD_B);
		exampleEntityBuilder.withName(ANY_ENTITY_NAME).withValue(exampleValueBuilder.build());
	}

	@Test
	public void shouldListExamplesAsJson() throws Exception {
		// given
		ArrayList<ExampleEntity> content = newArrayList(exampleEntityBuilder.build(), exampleEntityBuilder.build());

		when(exampleRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<ExampleEntity>(content));

		// when & then
		perform(get(RequestMappings.EXAMPLES).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andExpect(jsonPath("$.content").value(hasSize(content.size())))
				.andExpect(jsonPath("$.content[*].name").value(everyItem(equalTo(ANY_ENTITY_NAME))));
	}
}