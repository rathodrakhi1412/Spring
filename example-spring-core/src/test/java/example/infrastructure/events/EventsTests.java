package example.infrastructure.events;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.google.common.eventbus.Subscribe;

import example.TestGroups;
import example.domain.shared.ddd.AbstractEvent;
import example.domain.shared.ddd.EventPublisher;

@ContextConfiguration(classes = EventsConfig.class)
@Test(groups = { TestGroups.INTEGRATION }, singleThreaded = true)
@ActiveProfiles("test")
public class EventsTests extends AbstractTestNGSpringContextTests {

	private static final String ANY_PAYLOAD = "any payload";

	@Autowired
	private EventPublisher eventPublisher;

	@Autowired
	private TestEventSubscriber testEventSubscriber;

	@Test
	public void shouldReceiveEvent() {
		// given
		TestEvent event = new TestEvent(ANY_PAYLOAD);

		// when
		eventPublisher.publish(event);

		// then
		assertThat(testEventSubscriber.getEvent()).isEqualTo(event);
	}

	public static class TestEvent extends AbstractEvent<String> {

		private static final long serialVersionUID = 1L;

		public TestEvent(String payload) {
			super(payload);
		}

	}

	@Component
	public static class TestEventSubscriber {

		private TestEvent event;

		@Subscribe
		public void subscribe(TestEvent event) {
			this.event = event;
		}

		public TestEvent getEvent() {
			return event;
		}

	}
}
