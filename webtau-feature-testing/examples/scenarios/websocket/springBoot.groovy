/*
 * Copyright 2023 webtau maintainers
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package scenarios.websocket

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario("wait until receive message") {
    // connect-send-wait
    def wsSession = websocket.connect("/prices")
    wsSession.send([symbol: "IBM"])

    wsSession.received.waitTo == [
            price: greaterThan(100),
            symbol: "IBM"]
    wsSession.close()
    // connect-send-wait
}

scenario("wait until receive message using path") {
    def wsSession = websocket.connect("/prices")
    wsSession.send([symbol: "IBM"])

    // received-get
    wsSession.received.price.waitToBe > 100
    // received-get
    wsSession.close()
}

scenario("wait until receive message using path list") {
    def wsSession = websocket.connect("/prices")
    wsSession.send([symbol: "LIST"])

    // received-list
    wsSession.received[2].price.waitToBe > 30
    // received-list
    wsSession.close()
}

scenario("poll message after wait") {
    def wsSession = websocket.connect("/prices")
    wsSession.send([symbol: "IBM"])

    wsSession.received.waitTo == [
            price: greaterThan(100),
            symbol: "IBM"]

    // poll-after-wait
    def nextMessage = wsSession.received.pollAsText()
    nextMessage.should == "{\"symbol\":\"IBM\",\"price\":102}"

    def nextNextMessage = wsSession.received.pollAsText(100) // explicit timeout in milliseconds for new message to arrive
    nextNextMessage.should == "{\"symbol\":\"IBM\",\"price\":103}"
    // poll-after-wait

    wsSession.close()
}

scenario("poll message") {
    def wsSession = websocket.connect("/prices")
    wsSession.send([symbol: "DUMMY"])

    def messageOne = wsSession.received.pollAsText()
    messageOne.should == "{\"symbol\":\"DUMMY\",\"price\":0}"

    def messageTwo = wsSession.received.pollAsText(10)
    messageTwo.should == null

    wsSession.close()
}

scenario("poll map") {
    def wsSession = websocket.connect("/prices")
    wsSession.send([symbol: "IBM"])

    // poll-as-map
    def message = wsSession.received.poll()
    message.symbol.should == "IBM"
    // poll-as-map

    wsSession.close()
}

scenario("discard messages") {
    def wsSession = websocket.connect("/prices")
    wsSession.send([symbol: "IBM"])

    wsSession.received.count.waitTo == 53

    // discard-poll
    wsSession.received.discard()

    def nextMessage = wsSession.received.pollAsText(1)
    nextMessage.should == null
    // discard-poll

    wsSession.close()
}

scenario("special names to check no clash with groovy dsl") {
    def wsSession = websocket.connect("/prices")

    wsSession.send([symbol: "GROOVY_DSL"])
    wsSession.received.label.waitTo == "hello label"

    wsSession.send([symbol: "GROOVY_DSL"])
    wsSession.received.destination.waitTo == "hello destination"

    wsSession.close()
}
