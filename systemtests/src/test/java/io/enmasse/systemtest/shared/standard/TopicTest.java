/*
 * Copyright 2016-2018, EnMasse authors.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */

package io.enmasse.systemtest.shared.standard;

import io.enmasse.address.model.Address;
import io.enmasse.address.model.AddressBuilder;
import io.enmasse.systemtest.amqp.AmqpClient;
import io.enmasse.systemtest.bases.TestBase;
import io.enmasse.systemtest.bases.shared.ITestSharedStandard;
import io.enmasse.systemtest.clients.ClientUtils;
import io.enmasse.systemtest.logs.CustomLogger;
import io.enmasse.systemtest.model.address.AddressType;
import io.enmasse.systemtest.model.addressplan.DestinationPlan;
import io.enmasse.systemtest.resolvers.JmsProviderParameterResolver;
import io.enmasse.systemtest.utils.AddressSpaceUtils;
import io.enmasse.systemtest.utils.AddressUtils;
import io.enmasse.systemtest.utils.JmsProvider;
import io.enmasse.systemtest.utils.TestUtils;
import org.apache.qpid.proton.amqp.DescribedType;
import org.apache.qpid.proton.amqp.Symbol;
import org.apache.qpid.proton.amqp.messaging.AmqpValue;
import org.apache.qpid.proton.amqp.messaging.ApplicationProperties;
import org.apache.qpid.proton.amqp.messaging.Source;
import org.apache.qpid.proton.message.Message;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;

import javax.jms.Connection;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static io.enmasse.systemtest.TestTag.NON_PR;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(JmsProviderParameterResolver.class)
public class TopicTest extends TestBase implements ITestSharedStandard {
    private static Logger log = CustomLogger.getLogger();

    private static void runTopicTest(AmqpClient client, Address dest)
            throws InterruptedException, ExecutionException, TimeoutException, IOException {
        runTopicTest(client, dest, 512);
    }

    public static void runTopicTest(AmqpClient client, Address dest, int msgCount)
            throws InterruptedException, IOException, TimeoutException, ExecutionException {
        List<String> msgs = TestUtils.generateMessages(msgCount);
        Future<List<Message>> recvMessages = client.recvMessages(dest.getSpec().getAddress(), msgCount);
        long timeoutMs = msgCount * ClientUtils.ESTIMATE_MAX_MS_PER_MESSAGE;
        log.info("Start  sending with " + timeoutMs + " ms timeout");
        assertThat("Wrong count of messages sent",
                client.sendMessages(dest.getSpec().getAddress(), msgs).get(timeoutMs, TimeUnit.MILLISECONDS), is(msgs.size()));
        log.info("Start receiving with " + timeoutMs + " ms timeout");
        assertThat("Wrong count of messages received",
                recvMessages.get(timeoutMs, TimeUnit.MILLISECONDS).size(), is(msgs.size()));
    }

    @Test
    @Tag(NON_PR)
    void testColocatedTopics() throws Exception {
        Address t1 = new AddressBuilder()
                .withNewMetadata()
                .withNamespace(getSharedAddressSpace().getMetadata().getNamespace())
                .withName(AddressUtils.generateAddressMetadataName(getSharedAddressSpace(), "test-topic1-pooled"))
                .endMetadata()
                .withNewSpec()
                .withType("topic")
                .withAddress("test-topic1-pooled")
                .withPlan(DestinationPlan.STANDARD_SMALL_TOPIC)
                .endSpec()
                .build();
        Address t2 = new AddressBuilder()
                .withNewMetadata()
                .withNamespace(getSharedAddressSpace().getMetadata().getNamespace())
                .withName(AddressUtils.generateAddressMetadataName(getSharedAddressSpace(), "test-topic2-pooled"))
                .endMetadata()
                .withNewSpec()
                .withType("topic")
                .withAddress("test-topic2-pooled")
                .withPlan(DestinationPlan.STANDARD_SMALL_TOPIC)
                .endSpec()
                .build();
        Address t3 = new AddressBuilder()
                .withNewMetadata()
                .withNamespace(getSharedAddressSpace().getMetadata().getNamespace())
                .withName(AddressUtils.generateAddressMetadataName(getSharedAddressSpace(), "test-topic3-pooled"))
                .endMetadata()
                .withNewSpec()
                .withType("topic")
                .withAddress("test-topic3-pooled")
                .withPlan(DestinationPlan.STANDARD_SMALL_TOPIC)
                .endSpec()
                .build();
        resourcesManager.setAddresses(t1, t2, t3);

        AmqpClient client = getAmqpClientFactory().createTopicClient();
        runTopicTest(client, t1);
        runTopicTest(client, t2);
        runTopicTest(client, t3);
    }

    @Test
    void testShardedTopic() throws Exception {
        Address t1 = new AddressBuilder()
                .withNewMetadata()
                .withNamespace(getSharedAddressSpace().getMetadata().getNamespace())
                .withName(AddressUtils.generateAddressMetadataName(getSharedAddressSpace(), "test-topic1-sharded"))
                .endMetadata()
                .withNewSpec()
                .withType("topic")
                .withAddress("test-topic1-sharded")
                .withPlan(DestinationPlan.STANDARD_LARGE_TOPIC)
                .endSpec()
                .build();
        Address t2 = new AddressBuilder()
                .withNewMetadata()
                .withNamespace(getSharedAddressSpace().getMetadata().getNamespace())
                .withName(AddressUtils.generateAddressMetadataName(getSharedAddressSpace(), "test-topic2-sharded"))
                .endMetadata()
                .withNewSpec()
                .withType("topic")
                .withAddress("test-topic2-sharded")
                .withPlan(DestinationPlan.STANDARD_LARGE_TOPIC)
                .endSpec()
                .build();
        resourcesManager.setAddresses(t2);

        resourcesManager.appendAddresses(t1);
        AddressUtils.waitForDestinationsReady(t2);

        AmqpClient topicClient = getAmqpClientFactory().createTopicClient();
        runTopicTest(topicClient, t1, 2048);
        runTopicTest(topicClient, t2, 2048);
    }

    @Test
    @Tag(NON_PR)
    void testRestApi() throws Exception {
        Address t1 = new AddressBuilder()
                .withNewMetadata()
                .withNamespace(getSharedAddressSpace().getMetadata().getNamespace())
                .withName(AddressUtils.generateAddressMetadataName(getSharedAddressSpace(), "test-topic1-small"))
                .endMetadata()
                .withNewSpec()
                .withType("topic")
                .withAddress("test-topic1-small")
                .withPlan(DestinationPlan.STANDARD_SMALL_TOPIC)
                .endSpec()
                .build();
        Address t2 = new AddressBuilder()
                .withNewMetadata()
                .withNamespace(getSharedAddressSpace().getMetadata().getNamespace())
                .withName(AddressUtils.generateAddressMetadataName(getSharedAddressSpace(), "test-topic2-small"))
                .endMetadata()
                .withNewSpec()
                .withType("topic")
                .withAddress("test-topic2-small")
                .withPlan(DestinationPlan.STANDARD_SMALL_TOPIC)
                .endSpec()
                .build();

        assertAddressApi(getSharedAddressSpace(), t1, t2);
    }

    @Test
    @Tag(NON_PR)
    void testMessageSelectorsAppProperty() throws Exception {
        Address selTopic = new AddressBuilder()
                .withNewMetadata()
                .withNamespace(getSharedAddressSpace().getMetadata().getNamespace())
                .withName(AddressUtils.generateAddressMetadataName(getSharedAddressSpace(), "selector-topic-large"))
                .endMetadata()
                .withNewSpec()
                .withType("topic")
                .withAddress("selector-topic-large")
                .withPlan(DestinationPlan.STANDARD_LARGE_TOPIC)
                .endSpec()
                .build();
        String linkName = "linkSelectorTopicAppProp";
        resourcesManager.setAddresses(selTopic);

        AmqpClient topicClient = getAmqpClientFactory().createTopicClient();

        Map<String, Object> appProperties = new HashMap<>();
        appProperties.put("appPar1", 1);
        assertAppProperty(topicClient, linkName, appProperties, "appPar1 = 1", selTopic);

        appProperties.clear();
        appProperties.put("appPar2", 10);
        assertAppProperty(topicClient, linkName, appProperties, "appPar2 > 9", selTopic);

        appProperties.clear();
        appProperties.put("appPar3", 10);
        assertAppProperty(topicClient, linkName, appProperties, "appPar3 < 11", selTopic);

        appProperties.clear();
        appProperties.put("appPar4", 10);
        assertAppProperty(topicClient, linkName, appProperties, "appPar4 * 2 > 10", selTopic);

        appProperties.clear();
        appProperties.put("year", 2000);
        assertAppProperty(topicClient, linkName, appProperties, "(year > 1000) AND (year < 3000)", selTopic);

        appProperties.clear();
        appProperties.put("year2", 2000);
        assertAppProperty(topicClient, linkName, appProperties, "year2 BETWEEN 1999 AND 2018", selTopic);

        appProperties.clear();
        appProperties.put("appPar5", "1");
        assertAppProperty(topicClient, linkName, appProperties, "appPar5 = '1'", selTopic);

        appProperties.clear();
        appProperties.put("appPar6", true);
        assertAppProperty(topicClient, linkName, appProperties, "appPar6 = TRUE", selTopic);

        appProperties.clear();
        appProperties.put("appPar7", "SOMETHING");
        assertAppProperty(topicClient, linkName, appProperties, "appPar7 IS NOT NULL", selTopic);

        appProperties.clear();
        appProperties.put("appPar8", "SOMETHING");
        assertAppProperty(topicClient, linkName, appProperties, "appPar8 LIKE '%THING' ", selTopic);

        appProperties.clear();
        appProperties.put("appPar9", "bar");
        assertAppProperty(topicClient, linkName, appProperties, "appPar9 IN ('foo', 'bar', 'baz')", selTopic);

    }

    private void assertAppProperty(AmqpClient client, String linkName, Map<String, Object> appProperties, String selector, Address dest) throws Exception {
        log.info("Application property selector: " + selector);
        int msgsCount = 10;
        List<Message> listOfMessages = new ArrayList<>();
        for (int i = 0; i < msgsCount; i++) {
            Message msg = Message.Factory.create();
            msg.setAddress(dest.getSpec().getAddress());
            msg.setBody(new AmqpValue(dest.getSpec().getAddress()));
            msg.setSubject("subject");
            listOfMessages.add(msg);
        }

        //set appProperty for last message
        if (listOfMessages.size() > 0) {
            listOfMessages.get(msgsCount - 1).setApplicationProperties(new ApplicationProperties(appProperties));
        }

        Source source = new Source();
        source.setAddress(dest.getSpec().getAddress());
        source.setCapabilities(Symbol.getSymbol("topic"));
        Map<Symbol, DescribedType> map = new HashMap<>();
        map.put(Symbol.valueOf("jms-selector"), new AmqpJmsSelectorFilter(selector));
        source.setFilter(map);

        Future<List<Message>> received = client.recvMessages(source, linkName, 1);
        AmqpClient client2 = getAmqpClientFactory().createTopicClient();
        Future<List<Message>> receivedWithoutSel = client2.recvMessages(dest.getSpec().getAddress(), msgsCount - 1);

        Future<Integer> sent = client.sendMessages(dest.getSpec().getAddress(), listOfMessages.toArray(new Message[0]));

        assertThat("Wrong count of messages sent",
                sent.get(1, TimeUnit.MINUTES), is(msgsCount));
        assertThat("Wrong count of messages received",
                received.get(1, TimeUnit.MINUTES).size(), is(1));

        Map.Entry<String, Object> entry = appProperties.entrySet().iterator().next();
        assertThat("Wrong application property",
                received.get(1, TimeUnit.MINUTES)
                        .get(0)
                        .getApplicationProperties()
                        .getValue()
                        .get(entry.getKey()),
                is(entry.getValue()));

        //receive rest of messages
        assertThat("Wrong count of messages received",
                receivedWithoutSel.get(1, TimeUnit.MINUTES).size(), is(msgsCount - 1));
    }

    @Test
    void testMessageSelectorsProperty() throws Exception {
        Address selTopic = new AddressBuilder()
                .withNewMetadata()
                .withNamespace(getSharedAddressSpace().getMetadata().getNamespace())
                .withName(AddressUtils.generateAddressMetadataName(getSharedAddressSpace(), "prop-topic1"))
                .endMetadata()
                .withNewSpec()
                .withType("topic")
                .withAddress("prop-topic1")
                .withPlan(DestinationPlan.STANDARD_LARGE_TOPIC)
                .endSpec()
                .build();
        String linkName = "linkSelectorTopicProp";
        resourcesManager.setAddresses(selTopic);

        int msgsCount = 10;
        List<Message> listOfMessages = new ArrayList<>();
        for (int i = 0; i < msgsCount; i++) {
            Message msg = Message.Factory.create();
            msg.setAddress(selTopic.getSpec().getAddress());
            msg.setBody(new AmqpValue(selTopic.getSpec().getAddress()));
            msg.setSubject("subject");
            listOfMessages.add(msg);
        }

        //set property for last message
        String groupID = "testGroupID";
        listOfMessages.get(msgsCount - 1).setGroupId(groupID);

        Source source = new Source();
        source.setAddress(selTopic.getSpec().getAddress());
        source.setCapabilities(Symbol.getSymbol("topic"));
        Map<Symbol, DescribedType> map = new HashMap<>();
        map.put(Symbol.valueOf("jms-selector"), new AmqpJmsSelectorFilter("JMSXGroupID IS NOT NULL"));
        source.setFilter(map);

        AmqpClient client = getAmqpClientFactory().createTopicClient();
        Future<List<Message>> received = client.recvMessages(source, linkName, 1);

        Future<Integer> sent = client.sendMessages(selTopic.getSpec().getAddress(), listOfMessages.toArray(new Message[0]));

        assertThat("Wrong count of messages sent", sent.get(1, TimeUnit.MINUTES), is(msgsCount));

        assertThat("Wrong count of messages received",
                received.get(1, TimeUnit.MINUTES).size(), is(1));
        assertThat("Message with wrong groupID received",
                received.get(1, TimeUnit.MINUTES).get(0).getGroupId(), is(groupID));
    }

    @Test
    void testDurableSubscriptionOnPooledTopic() throws Exception {
        Address topic = new AddressBuilder()
                .withNewMetadata()
                .withNamespace(getSharedAddressSpace().getMetadata().getNamespace())
                .withName(AddressUtils.generateAddressMetadataName(getSharedAddressSpace(), "test-topic-pooled"))
                .endMetadata()
                .withNewSpec()
                .withType("topic")
                .withAddress("test-topic-pooled")
                .withPlan(DestinationPlan.STANDARD_SMALL_TOPIC)
                .endSpec()
                .build();
        Address subscription = new AddressBuilder()
                .withNewMetadata()
                .withNamespace(getSharedAddressSpace().getMetadata().getNamespace())
                .withName(AddressUtils.generateAddressMetadataName(getSharedAddressSpace(), "mysub-pooled"))
                .endMetadata()
                .withNewSpec()
                .withType("subscription")
                .withTopic(topic.getSpec().getAddress())
                .withAddress("mysub-pooled")
                .withPlan(DestinationPlan.STANDARD_SMALL_SUBSCRIPTION)
                .endSpec()
                .build();
        resourcesManager.setAddresses(topic, subscription);

        AmqpClient client = getAmqpClientFactory().createTopicClient();
        List<String> batch1 = Arrays.asList("one", "two", "three");

        log.info("Receiving first batch");
        Future<List<Message>> recvResults = client.recvMessages(AddressUtils.getQualifiedSubscriptionAddress(subscription), batch1.size());

        log.info("Sending first batch");
        assertThat("Wrong count of messages sent: batch1",
                client.sendMessages(topic.getSpec().getAddress(), batch1).get(1, TimeUnit.MINUTES), is(batch1.size()));
        assertThat("Wrong messages received: batch1", extractBodyAsString(recvResults), is(batch1));

        log.info("Sending second batch");
        List<String> batch2 = Arrays.asList("four", "five", "six", "seven", "eight", "nine", "ten", "eleven", "twelve");
        assertThat("Wrong messages sent: batch2",
                client.sendMessages(topic.getSpec().getAddress(), batch2).get(1, TimeUnit.MINUTES), is(batch2.size()));

        log.info("Receiving second batch");
        recvResults = client.recvMessages(AddressUtils.getQualifiedSubscriptionAddress(subscription), batch2.size());
        assertThat("Wrong messages received: batch2", extractBodyAsString(recvResults), is(batch2));
    }

    @Test
    void testDurableSubscriptionMaxConsumers() throws Exception {
        Address topic = new AddressBuilder()
                .withNewMetadata()
                .withNamespace(getSharedAddressSpace().getMetadata().getNamespace())
                .withName(AddressUtils.generateAddressMetadataName(getSharedAddressSpace(), "test-topic-max-sub"))
                .endMetadata()
                .withNewSpec()
                .withType("topic")
                .withAddress("test-topic-max-sub")
                .withPlan(DestinationPlan.STANDARD_SMALL_TOPIC)
                .endSpec()
                .build();
        Address subscription = new AddressBuilder()
                .withNewMetadata()
                .withNamespace(getSharedAddressSpace().getMetadata().getNamespace())
                .withName(AddressUtils.generateAddressMetadataName(getSharedAddressSpace(), "mysub-max"))
                .endMetadata()
                .withNewSpec()
                .withType("subscription")
                .withTopic(topic.getSpec().getAddress())
                .withAddress("mysub-max")
                .editOrNewSubscription()
                .withMaxConsumers(1)
                .endSubscription()
                .withPlan(DestinationPlan.STANDARD_SMALL_SUBSCRIPTION)
                .endSpec()
                .build();
        resourcesManager.setAddresses(topic, subscription);

        // Start two consumers and check that the second consumer is disconnected
        AmqpClient client = getAmqpClientFactory().createTopicClient();
        Future<List<Message>> c1 = client.recvMessages(AddressUtils.getQualifiedSubscriptionAddress(subscription), 0);
        Future<List<Message>> c2 = client.recvMessages(AddressUtils.getQualifiedSubscriptionAddress(subscription), 0);

        // Second consumer should get an error
        Exception thrown = assertThrows(Exception.class, () -> c2.get(10, TimeUnit.SECONDS));
        assertTrue(thrown.getMessage().contains("Maximum Consumer Limit Reached"), "Unexpected exception message: " + thrown.getMessage());
    }

    @Test
    void testDurableSubscriptionMultipleConsumers() throws Exception {
        Address topic = new AddressBuilder()
                .withNewMetadata()
                .withNamespace(getSharedAddressSpace().getMetadata().getNamespace())
                .withName(AddressUtils.generateAddressMetadataName(getSharedAddressSpace(), "test-topic-shared-sub"))
                .endMetadata()
                .withNewSpec()
                .withType("topic")
                .withAddress("test-topic-shared-sub")
                .withPlan(DestinationPlan.STANDARD_SMALL_TOPIC)
                .endSpec()
                .build();
        Address subscription = new AddressBuilder()
                .withNewMetadata()
                .withNamespace(getSharedAddressSpace().getMetadata().getNamespace())
                .withName(AddressUtils.generateAddressMetadataName(getSharedAddressSpace(), "mysub-shared"))
                .endMetadata()
                .withNewSpec()
                .withType("subscription")
                .withTopic(topic.getSpec().getAddress())
                .withAddress("mysub-shared")
                .editOrNewSubscription()
                .withMaxConsumers(2)
                .endSubscription()
                .withPlan(DestinationPlan.STANDARD_SMALL_SUBSCRIPTION)
                .endSpec()
                .build();
        resourcesManager.setAddresses(topic, subscription);

        AmqpClient client = getAmqpClientFactory().createTopicClient();

        log.info("Start consumers");
        Future<List<Message>> c1 = client.recvMessages(AddressUtils.getQualifiedSubscriptionAddress(subscription), 10);
        Future<List<Message>> c2 = client.recvMessages(AddressUtils.getQualifiedSubscriptionAddress(subscription), 10);

        log.info("Starting producer");
        assertEquals((Integer)20, client.sendMessages(topic.getSpec().getAddress(), TestUtils.generateMessages(20)).get(1, TimeUnit.MINUTES));

        int c1Result = c1.get(1, TimeUnit.MINUTES).size();
        int c2Result = c2.get(1, TimeUnit.MINUTES).size();
        log.info("C1 = {}, C2 = {}", c1Result, c2Result);
        assertEquals(20, c1Result + c2Result);
    }

    @Test
    void testDurableSubscriptionOnShardedTopic() throws Exception {
        Address topic = new AddressBuilder()
                .withNewMetadata()
                .withNamespace(getSharedAddressSpace().getMetadata().getNamespace())
                .withName(AddressUtils.generateAddressMetadataName(getSharedAddressSpace(), "test-topic-sharded"))
                .endMetadata()
                .withNewSpec()
                .withType("topic")
                .withAddress("test-topic-sharded")
                .withPlan(DestinationPlan.STANDARD_SMALL_TOPIC)
                .endSpec()
                .build();
        Address subscription1 = new AddressBuilder()
                .withNewMetadata()
                .withNamespace(getSharedAddressSpace().getMetadata().getNamespace())
                .withName(AddressUtils.generateAddressMetadataName(getSharedAddressSpace(), "mysub"))
                .endMetadata()
                .withNewSpec()
                .withType("subscription")
                .withTopic(topic.getSpec().getAddress())
                .withAddress("mysub")
                .withPlan(DestinationPlan.STANDARD_SMALL_SUBSCRIPTION)
                .endSpec()
                .build();
        Address subscription2 = new AddressBuilder()
                .withNewMetadata()
                .withNamespace(getSharedAddressSpace().getMetadata().getNamespace())
                .withName(AddressUtils.generateAddressMetadataName(getSharedAddressSpace(), "anothersub"))
                .endMetadata()
                .withNewSpec()
                .withType("subscription")
                .withTopic(topic.getSpec().getAddress())
                .withAddress("anothersub")
                .withPlan(DestinationPlan.STANDARD_SMALL_SUBSCRIPTION)
                .endSpec()
                .build();
        resourcesManager.setAddresses(topic, subscription1, subscription2);

        AmqpClient client = getAmqpClientFactory().createTopicClient();
        List<String> batch1 = Arrays.asList("one", "two", "three");

        log.info("Receiving first batch");
        Future<List<Message>> recvResults = client.recvMessages(AddressUtils.getQualifiedSubscriptionAddress(subscription1), batch1.size());

        log.info("Sending first batch");
        assertThat("Wrong count of messages sent: batch1",
                client.sendMessages(topic.getSpec().getAddress(), batch1).get(1, TimeUnit.MINUTES), is(batch1.size()));
        assertThat("Wrong messages received: batch1", extractBodyAsString(recvResults), is(batch1));

        log.info("Sending second batch");
        List<String> batch2 = Arrays.asList("four", "five", "six", "seven", "eight", "nine", "ten", "eleven", "twelve");
        assertThat("Wrong count of messages sent: batch2",
                client.sendMessages(topic.getSpec().getAddress(), batch2).get(1, TimeUnit.MINUTES), is(batch2.size()));

        log.info("Receiving second batch");
        recvResults = client.recvMessages(AddressUtils.getQualifiedSubscriptionAddress(subscription1), batch2.size());
        assertThat("Wrong messages received: batch2", extractBodyAsString(recvResults), is(batch2));

        log.info("Receiving messages from second subscription");
        List<String> allmessages = new ArrayList<>(batch1);
        allmessages.addAll(batch2);
        AmqpClient client2 = getAmqpClientFactory().createTopicClient();
        recvResults = client2.recvMessages(AddressUtils.getQualifiedSubscriptionAddress(subscription2), allmessages.size());
        assertThat("Wrong messages received for second subscription", extractBodyAsString(recvResults), is(allmessages));
    }

    @Test
    void testDurableSubscriptionOnShardedTopic2() throws Exception {
        Address topic = new AddressBuilder()
                .withNewMetadata()
                .withNamespace(getSharedAddressSpace().getMetadata().getNamespace())
                .withName(AddressUtils.generateAddressMetadataName(getSharedAddressSpace(), "test-topic-durable-sharded"))
                .endMetadata()
                .withNewSpec()
                .withType("topic")
                .withAddress("test-topic-durable-sharded")
                .withPlan(DestinationPlan.STANDARD_LARGE_TOPIC)
                .endSpec()
                .build();
        Address subscription1 = new AddressBuilder()
                .withNewMetadata()
                .withNamespace(getSharedAddressSpace().getMetadata().getNamespace())
                .withName(AddressUtils.generateAddressMetadataName(getSharedAddressSpace(), "mysub2"))
                .endMetadata()
                .withNewSpec()
                .withType("subscription")
                .withTopic(topic.getSpec().getAddress())
                .withAddress("mysub2")
                .withPlan(DestinationPlan.STANDARD_SMALL_SUBSCRIPTION)
                .endSpec()
                .build();
        resourcesManager.setAddresses(topic, subscription1);

        AmqpClient client = getAmqpClientFactory().createTopicClient();
        List<String> batch1 = Arrays.asList("one", "two", "three");

        log.info("Sending first batch");
        assertThat("Wrong count of messages sent: batch1",
                client.sendMessages(topic.getSpec().getAddress(), batch1).get(1, TimeUnit.MINUTES), is(batch1.size()));

        log.info("Receiving first batch");
        Future<List<Message>> recvResults = client.recvMessages(AddressUtils.getQualifiedSubscriptionAddress(subscription1), batch1.size());
        assertThat("Wrong messages received: batch1", extractBodyAsString(recvResults), is(batch1));

        log.info("Creating second subscription");
        Address subscription2 = new AddressBuilder()
                .withNewMetadata()
                .withNamespace(getSharedAddressSpace().getMetadata().getNamespace())
                .withName(AddressUtils.generateAddressMetadataName(getSharedAddressSpace(), "anothersub"))
                .endMetadata()
                .withNewSpec()
                .withType("subscription")
                .withTopic(topic.getSpec().getAddress())
                .withAddress("anothersub")
                .withPlan(DestinationPlan.STANDARD_SMALL_SUBSCRIPTION)
                .endSpec()
                .build();
        resourcesManager.appendAddresses(subscription2);

        log.info("Sending second batch");
        List<String> batch2 = Arrays.asList("four", "five", "six", "seven", "eight", "nine", "ten", "eleven", "twelve");

        assertThat("Wrong count of messages sent: batch2",
                client.sendMessages(topic.getSpec().getAddress(), batch2).get(1, TimeUnit.MINUTES), is(batch2.size()));
        log.info("Receiving second batch");
        recvResults = client.recvMessages(AddressUtils.getQualifiedSubscriptionAddress(subscription1), batch2.size());
        assertThat("Wrong messages received: batch2", extractBodyAsString(recvResults), is(batch2));

        log.info("Receiving messages from second subscription");
        AmqpClient client2 = getAmqpClientFactory().createTopicClient();
        recvResults = client2.recvMessages(AddressUtils.getQualifiedSubscriptionAddress(subscription2), batch2.size());
        assertThat("Wrong messages received for second subscription", extractBodyAsString(recvResults), is(batch2));
    }

    @Test
    void testTopicWildcardsSharded() throws Exception {
        doTopicWildcardTest(DestinationPlan.STANDARD_LARGE_TOPIC);
    }

    @Test
    void testTopicWildcardsPooled() throws Exception {
        doTopicWildcardTest(DestinationPlan.STANDARD_SMALL_TOPIC);
    }

    private void doTopicWildcardTest(String plan) throws Exception {
        Address t0 = new AddressBuilder()
                .withNewMetadata()
                .withNamespace(getSharedAddressSpace().getMetadata().getNamespace())
                .withName(AddressUtils.generateAddressMetadataName(getSharedAddressSpace(), "topic-wild"))
                .endMetadata()
                .withNewSpec()
                .withType("topic")
                .withAddress("topic-wild")
                .withPlan(plan)
                .endSpec()
                .build();
        resourcesManager.setAddresses(t0);

        AmqpClient amqpClient = getAmqpClientFactory().createTopicClient();

        List<String> msgs = Arrays.asList("foo", "bar", "baz", "qux");

        Future<List<Message>> recvResults = amqpClient.recvMessages("topic-wild/#", msgs.size() * 3);

        amqpClient.sendMessages(t0.getSpec().getAddress() + "/foo", msgs);
        amqpClient.sendMessages(t0.getSpec().getAddress() + "/bar", msgs);
        amqpClient.sendMessages(t0.getSpec().getAddress() + "/baz/foobar", msgs);

        assertThat("Wrong count of messages received",
                recvResults.get(1, TimeUnit.MINUTES).size(), is(msgs.size() * 3));

        recvResults = amqpClient.recvMessages("topic-wild/world/+", msgs.size() * 2);

        amqpClient.sendMessages(t0.getSpec().getAddress() + "/world/africa", msgs);
        amqpClient.sendMessages(t0.getSpec().getAddress() + "/world/europe", msgs);
        amqpClient.sendMessages(t0.getSpec().getAddress() + "/world/asia/maldives", msgs);

        assertThat("Wrong count of messages received",
                recvResults.get(1, TimeUnit.MINUTES).size(), is(msgs.size() * 2));
    }

    @Test
    @DisplayName("testLargeMessages")
    void testLargeMessages(JmsProvider jmsProvider) throws Exception {
        Address addressTopic = new AddressBuilder()
                .withNewMetadata()
                .withNamespace(getSharedAddressSpace().getMetadata().getNamespace())
                .withName(AddressUtils.generateAddressMetadataName(getSharedAddressSpace(), "jms-topic-large"))
                .endMetadata()
                .withNewSpec()
                .withType("topic")
                .withAddress("jmsTopicLarge")
                .withPlan(getDefaultPlan(AddressType.TOPIC))
                .endSpec()
                .build();
        resourcesManager.setAddresses(addressTopic);

        Connection connection = jmsProvider.createConnection(AddressSpaceUtils.getMessagingRoute(getSharedAddressSpace()).toString(), defaultCredentials,
                "jmsCliId", addressTopic);
        connection.start();

        assertSendReceiveLargeMessageTopic(jmsProvider, 1, addressTopic, 1);
        assertSendReceiveLargeMessageTopic(jmsProvider, 0.5, addressTopic, 1);
        assertSendReceiveLargeMessageTopic(jmsProvider, 0.25, addressTopic, 1);

        connection.stop();
        connection.close();
    }


    static class AmqpJmsSelectorFilter implements DescribedType {

        private final String selector;

        AmqpJmsSelectorFilter(String selector) {
            this.selector = selector;
        }

        @Override
        public Object getDescriptor() {
            return Symbol.valueOf("apache.org:selector-filter:string");
        }

        @Override
        public Object getDescribed() {
            return this.selector;
        }

        @Override
        public String toString() {
            return "AmqpJmsSelectorType{" + selector + "}";
        }
    }
}
