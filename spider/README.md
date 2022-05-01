# Spider
Web crawler.

# Problem Statement
We'd like you to write a simple web crawler in a programming language you're familiar with. Given a starting URL, the crawler should visit each URL it finds on the same domain. It should print each URL visited, and a list of links found on that page. The crawler should be limited to one subdomain - so when you start with *https://monzo.com/*, it would crawl all pages on the monzo.com website, but not follow external links, for example to facebook.com or community.monzo.com.

We would like to see your own implementation of a web crawler. Please do not use frameworks like scrapy or go-colly which handle all the crawling behind the scenes or someone else's code. You are welcome to use libraries to handle things like HTML parsing.

Ideally, write it as you would a production piece of code. This exercise is not meant to show us whether you can write code – we are more interested in how you design software. This means that we care less about a fancy UI or sitemap format, and more about how your program is structured: the trade-offs you've made, what behaviour the program exhibits, and your use of concurrency, test coverage, and so on.

# Design
Key design decisions:
## API Design
- The entry point or controller for the crawler is a class called Spider which has the crawl() method
- The crawl method takes a URL as a String to make it easy to use so consumers do not have to create a URL or similar type in order to use the crawler
- The crawl method also takes a PageVisitor. This uses the Visitor pattern and allows the crawl outputs to be customised by the consumer. It is used in the console application to write the outputs to console and in the SpiderTest class to save the outputs so that they can be asserted upon
## Implementation Design
- The Spider class itself controls the overall flow of the crawl but delegates detailed logic
- The HTML parsing is done by the JSoupPageParser implementing the PageParser interface. This allows the parsing to be injected into the Spider and is used in the unit tests to mock the external calls and allow fast and reliable unit tests with no external dependencies or slow test fixtures. I considered testing using a local web server spun up with Docker, but decided this would be slow and over complex.
- Dependency injection is manual. A DI framework like Spring could have been used but this keeps the project dependencies to a minimum and makes the project more lightweight.
- The handling of links with absolute or relative paths and ability to compare links to see if they are internal or external is all done with the Link class which has separate unit tests to cover all the types of links.
## Test Design
- JUnit 5 and mockito are used.
- The main SpiderTest class user some test helpers to make the tests readable and hide the mocking code which can otherwise get in the way of readability. It takes some effort to refactor the test code to achieve this, but once done makes it easy to add new tests. The helpers also include a PageVisitor implementation that allows asserting on the results.
- Additional unit tests are only written for the Link class which has the key business logic used by the crawler. These tests are easier to write and maintain than the Spider tests, so the aim would be to have more tests at this level and use the Spider tests just for high level flows. E.g. a single Spider test to test internal vs external links are handled correctly, but many Link tests to test different types of internal and external links.
## Performance
- Added FixedThreadPool Executor for concurrent processing of pages
- TODO - work out a more elegant way of detecting completion other than busy wait
- TODO - Spider uses synchronized blocks to update context and to prevent PageVisitor being called concurrently. Look into better ways of maintaining thread safety.
