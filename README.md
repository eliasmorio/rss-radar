# RSS Feed Search Engine

## Overview

This project is an RSS feed search engine that gathers articles from various RSS feeds and stores them in a database, enabling efficient article search. The system follows an event-driven architecture, ensuring scalability and parallel processing of RSS feeds.

## Technologies

- **Backend**: Java 21, Spring Boot 3.3, JPA, Liquibase
- **Database**: PostgreSQL 16
- **Message Queue**: RabbitMQ 4.4
- **Frontend**: Angular 18

## Structure

The project is a multi-module Maven application with the following modules:

- **Web API**: A Spring Boot web application that serves the frontend, manages data, and provides an API for the worker module.
- **Worker**: A background Spring Boot application that processes messages from RabbitMQ, fetching articles from RSS feeds.
- **Common**: A shared module containing common utilities and Liquibase migration scripts, used across all modules.
- **Frontend**: An Angular-based user interface that allows users to search for articles and manage RSS feeds.

## Installation

To run the project locally, follow these steps:

1. Start the necessary containers with Docker:
    ```bash
    docker-compose up
    ```
   This will launch PostgreSQL and RabbitMQ containers.

2. Use the run configurations available in the `.run` folder in IntelliJ to start the backend and frontend applications.

## Design

The system consists of five main components:

- **Frontend**: An Angular web application for users to search through articles and RSS feeds.
- **Web API**: A Spring Boot backend that serves data to the frontend and exposes endpoints for the worker module.
- **Worker**: A background service that processes RSS feeds asynchronously by fetching new articles and content based on messages from RabbitMQ.
- **Database**: A PostgreSQL database storing articles and RSS feed metadata.
- **Message Queues**: RabbitMQ handles task management, allowing for parallel processing of feed data by the worker module.

This event-driven architecture allows workers to operate independently, ensuring that the system scales efficiently and can process multiple RSS feeds simultaneously.

## High-Level Architecture

![High-Level Architecture](./doc/architectureHighLevel.svg)

## Flow Diagram

![Flow Diagram](./doc/flow.svg)

---

## Key Features

### Search Engine

The system provides a full-text search capability, enabling users to search articles by keywords or phrases, even with large datasets.

In the project’s earlier versions (as part of a school project), we encountered performance issues after three months in production. The initial implementation used simple SQL queries for search, which proved inefficient.

To address this, I now use PostgreSQL’s full-text search feature, which transforms article content into searchable vectors. This, combined with GIN indexes, ensures fast and relevant search results, even with a large volume of articles.

### Event-Driven Architecture

The system is redesigned around an event-driven model with RabbitMQ as a central component.

The worker module handles a significant workload, especially when dealing with numerous or large RSS feeds. Decoupling the worker from the main application allows for easy scaling—additional workers can be added to manage the increased load. This asynchronous, parallel processing ensures efficient handling of RSS feeds, no matter their size or volume.

### Feed Exploration

This project aims to build a search engine that finds relevant articles. The approach involves exploring new feeds based on articles retrieved from the existing RSS feeds. Each time we fetch a new article, we search for new feeds within its content, allowing continuous feed exploration.

By assuming that the initial set of feeds is relevant to our users, we hypothesize that the new feeds found within their content will also be of interest.

### Deployment - CI/CD

The system is designed for deployment on a Kubernetes cluster. A CI/CD pipeline is configured to automatically build and publish Docker images. Deployment specifications are stored in the `deployment` folder.

### Potential Improvements

- Implement classification systems for articles based on their content.
- Support parsing podcasts and video feeds.
- Consider integrating Elasticsearch for more advanced search capabilities.