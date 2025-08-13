# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a reactive programming learning repository focused on Project Reactor (Spring WebFlux's default reactive framework). The codebase demonstrates reactive streams concepts, backpressure handling, and various Reactor operators through practical examples.

## Key Technologies

- **Project Reactor 3.4.0**: Core reactive programming library
- **Java 8**: Target language version
- **Maven**: Build system with maven-plugin packaging
- **JUnit 3.8.1**: Testing framework

## Project Structure

```
framework/webflux/
├── docs/image/           # Documentation images (UML diagrams)
├── project-reactor/      # Main Reactor examples
│   ├── src/main/java/com/github/opensharing/
│   │   ├── ReactorTester.java    # Basic Reactor operations
│   │   └── ReactorTester2.java   # Advanced Reactor patterns
│   ├── pom.xml           # Maven configuration
│   └── readme.md         # Detailed Reactor documentation
├── readme.md            # Reactive programming concepts overview
└── CLAUDE.md            # This file
```

## Common Commands

### Build and Run
```bash
# Build the project
cd project-reactor && mvn clean compile

# Run specific test classes
mvn exec:java -Dexec.mainClass="com.github.opensharing.ReactorTester"
mvn exec:java -Dexec.mainClass="com.github.opensharing.ReactorTester2"

# Run with specific method focus (update main method to call desired test)
```

### Development Workflow

1. **Test Individual Features**: Each test method in ReactorTester classes is designed to run independently
2. **Enable/Disable Tests**: Use commented method calls in main() to select which test to run
3. **Thread.sleep()**: Many examples include sleep calls to observe async behavior - adjust as needed

## Core Learning Areas

### Reactive Streams Concepts
- **Publisher/Subscriber pattern**: See ReactorTester.java:85-99 for custom Publisher implementation
- **Backpressure handling**: See ReactorTester2.java:233-295 for BaseSubscriber and backpressure strategies
- **Hot vs Cold streams**: See ReactorTester2.java:303-376 for ConnectableFlux examples

### Key Reactor Features Demonstrated

#### Basic Operations (ReactorTester)
- Flux/Mono creation: `just()`, `fromArray()`, `fromIterable()`, `range()`
- Stream transformations: `map()`, `filter()`, `collect()`, `reduce()`
- Stream combinations: `concat()`, `merge()`, `zip()`, `combineLatest()`
- Time-based operations: `delayElements()`, `interval()`, `sample()`

#### Advanced Patterns (ReactorTester2)
- Error handling: `onErrorReturn()`, `onErrorResume()`, `retryWhen()`
- Backpressure strategies: `onBackpressureBuffer()`, `onBackpressureDrop()`, `onBackpressureLatest()`
- Stream creation patterns: `push()`, `create()`, `generate()`
- Thread scheduling: `Schedulers.parallel()`, `Schedulers.boundedElastic()`

## Testing Approach

- **Interactive testing**: Run individual test methods to observe behavior
- **Thread.sleep()**: Used to observe async/timed operations
- **Console output**: Each test method includes detailed logging
- **Method isolation**: Each test method is self-contained and can run independently

## Important Notes

- **Java 8 syntax**: Code uses Java 8 features (lambdas, method references)
- **Async nature**: Many operations are non-blocking; use appropriate waiting mechanisms
- **Resource management**: Examples show proper handling of disposable resources
- **Error handling**: Comprehensive error handling patterns demonstrated throughout

## Quick Start

1. Navigate to `project-reactor/` directory
2. Run `mvn compile` to build
3. Run `mvn exec:java -Dexec.mainClass="com.github.opensharing.ReactorTester"` to see basic examples
4. Modify the main() method to call specific test methods based on what you want to learn