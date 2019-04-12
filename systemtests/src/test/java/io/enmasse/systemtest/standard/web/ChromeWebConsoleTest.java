/*
 * Copyright 2018, EnMasse authors.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.enmasse.systemtest.standard.web;

import io.enmasse.systemtest.AddressType;
import io.enmasse.systemtest.DestinationPlan;
import io.enmasse.systemtest.UserCredentials;
import io.enmasse.systemtest.ability.ITestBaseStandard;
import io.enmasse.systemtest.bases.web.WebConsoleTest;
import io.enmasse.systemtest.selenium.ISeleniumProviderChrome;
import io.enmasse.systemtest.utils.AddressUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.enmasse.systemtest.TestTag.nonPR;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag(nonPR)
public class ChromeWebConsoleTest extends WebConsoleTest implements ITestBaseStandard, ISeleniumProviderChrome {

    @Test
    void testCreateDeleteQueue() throws Exception {
        doTestCreateDeleteAddress(AddressUtils.createQueueAddressObject("test-queue1", DestinationPlan.STANDARD_SMALL_QUEUE),
                AddressUtils.createQueueAddressObject("test-queue2", DestinationPlan.STANDARD_LARGE_QUEUE));
    }

    @Test
    @Disabled("Only few chrome tests are enabled, rest functionality is covered by firefox")
    void testCreateDeleteTopic() throws Exception {
        doTestCreateDeleteAddress(AddressUtils.createTopicAddressObject("test-topic1", DestinationPlan.STANDARD_SMALL_TOPIC),
                AddressUtils.createTopicAddressObject("test-topic2", DestinationPlan.STANDARD_LARGE_TOPIC));
    }

    @Test
    @Disabled("Only few chrome tests are enabled, rest functionality is covered by firefox")
    void testCreateDeleteDurableSubscription() throws Exception {
        doTestCreateDeleteDurableSubscription(AddressUtils.createTopicAddressObject("test-topic1", DestinationPlan.STANDARD_SMALL_TOPIC),
                AddressUtils.createTopicAddressObject("test-topic2", DestinationPlan.STANDARD_LARGE_TOPIC));
    }

    @Test
    void testCreateDeleteAnycast() throws Exception {
        doTestCreateDeleteAddress(AddressUtils.createAnycastAddressObject("test-anycast-chrome"));
    }

    @Test
    @Disabled("Only few chrome tests are enabled, rest functionality is covered by firefox")
    void testCreateDeleteMulticast() throws Exception {
        doTestCreateDeleteAddress(AddressUtils.createMulticastAddressObject("test-multicast-chrome"));
    }

    @Test
    void testFilterAddressesByType() throws Exception {
        doTestFilterAddressesByType();
    }

    @Test
    @Disabled("Only few chrome tests are enabled, rest functionality is covered by firefox")
    void testFilterAddressesByName() throws Exception {
        doTestFilterAddressesByName();
    }

    @Test
    @Disabled("Only few chrome tests are enabled, rest functionality is covered by firefox")
    void testDeleteFilteredAddress() throws Exception {
        doTestDeleteFilteredAddress();
    }

    @Test
    @Disabled("Only few chrome tests are enabled, rest functionality is covered by firefox")
    void testFilterAddressWithRegexSymbols() throws Exception {
        doTestFilterAddressWithRegexSymbols();
    }

    @Test
    @Disabled("Only few chrome tests are enabled, rest functionality is covered by firefox")
    void testRegexAlertBehavesConsistently() throws Exception {
        doTestRegexAlertBehavesConsistently();
    }

    @Test
    void testSortAddressesByName() throws Exception {
        doTestSortAddressesByName();
    }

    @Test
    @Disabled("Only few chrome tests are enabled, rest functionality is covered by firefox")
    void testSortConnectionsBySenders() throws Exception {
        doTestSortConnectionsBySenders();
    }

    @Test
    @Disabled("Only few chrome tests are enabled, rest functionality is covered by firefox")
    void testSortConnectionsByReceivers() throws Exception {
        doTestSortConnectionsByReceivers();
    }

    @Test
    @Disabled("Only few chrome tests are enabled, rest functionality is covered by firefox")
    void testFilterConnectionsByEncrypted() throws Exception {
        doTestFilterConnectionsByEncrypted();
    }

    @Test
    @Disabled("Only few chrome tests are enabled, rest functionality is covered by firefox")
    void testFilterConnectionsByUser() throws Exception {
        doTestFilterConnectionsByUser();
    }

    @Test
    @Disabled("Only few chrome tests are enabled, rest functionality is covered by firefox")
    void testFilterConnectionsByHostname() throws Exception {
        doTestFilterConnectionsByHostname();
    }

    @Test
    @Disabled("Only few chrome tests are enabled, rest functionality is covered by firefox")
    void testSortConnectionsByHostname() throws Exception {
        doTestSortConnectionsByHostname();
    }

    @Test
    @Disabled("Only few chrome tests are enabled, rest functionality is covered by firefox")
    void testFilterConnectionsByContainerId() throws Exception {
        doTestFilterConnectionsByContainerId();
    }

    @Test
    @Disabled("Only few chrome tests are enabled, rest functionality is covered by firefox")
    void testSortConnectionsByContainerId() throws Exception {
        doTestSortConnectionsByContainerId();
    }

    @Test
    @Disabled("Only few chrome tests are enabled, rest functionality is covered by firefox")
    void testMessagesMetrics() throws Exception {
        doTestMessagesMetrics();
    }

    @Test
    @Disabled("Only few chrome tests are enabled, rest functionality is covered by firefox")
    void testClientsMetrics() throws Exception {
        doTestClientsMetrics();
    }

    @Test()
    void testCannotOpenConsolePage() {
        assertThrows(IllegalAccessException.class, () -> doTestCanOpenConsolePage(new UserCredentials("noexistuser", "pepaPa555")));
    }

    @Test
    @Disabled("Only few chrome tests are enabled, rest functionality is covered by firefox")
    void testCanOpenConsolePage() throws Exception {
        doTestCanOpenConsolePage(clusterUser);
    }

    @Test
    @Disabled("Only few chrome tests are enabled, rest functionality is covered by firefox")
    void testCreateAddressWithSpecialCharsShowsErrorMessage() throws Exception {
        doTestCreateAddressWithSpecialCharsShowsErrorMessage();
    }

    @Test
    @Disabled("Only a few chrome tests are enabled, rest of functionality is covered by firefox")
    void testCreateAddressWithSymbolsAt61stCharIndex() throws Exception {
        doTestCreateAddressWithSymbolsAt61stCharIndex(
                AddressUtils.createQueueAddressObject("queue10charHere-10charHere-10charHere-10charHere-10charHere-1",
                        getDefaultPlan(AddressType.QUEUE)),
                AddressUtils.createQueueAddressObject("queue10charHere-10charHere-10charHere-10charHere-10charHere.1",
                        getDefaultPlan(AddressType.QUEUE)));
    }

    @Test
    @Disabled("Only a few chrome tests are enabled, rest of functionality is covered by firefox")
    void testAddressWithValidPlanOnly() throws Exception {
        doTestAddressWithValidPlanOnly();
    }

    @Override
    public boolean skipDummyAddress() {
        return true;
    }
}
