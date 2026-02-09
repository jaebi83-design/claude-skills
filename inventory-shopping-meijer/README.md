# Inventory Shopping Meijer

An Android app that optimizes your Meijer grocery shopping trip by organizing an unordered grocery list into aisle-sequential order.

## Problem

You have an unordered grocery list and need to shop efficiently at Meijer. Walking back and forth across the store wastes time.

## Solution

The app maps each grocery item to its Meijer store aisle and outputs a sequential list sorted by aisle (aisle 1 through the last aisle), so you walk the store in one efficient pass. Items that can't be mapped go to an "Unknown" section at the end.

## Features

- **Aisle-sorted shopping list** — items grouped by store section in walk-order
- **Three-tier item matching** — exact product match, category keyword match, and heuristic inference
- **Self-learning database** — every manual aisle assignment is saved for future use
- **Check-off items** — tap to mark items as collected while shopping
- **Clipboard import** — paste a grocery list from any app (including Skylight)
- **Pre-seeded data** — ships with 25 store sections, 93 common product mappings, and 26 category keyword groups

## Tech Stack

- **Kotlin** + **Jetpack Compose** + **Material 3**
- **Room** for local database
- **Hilt** for dependency injection
- **MVVM + Clean Architecture**
- **Ktor Client** (future Skylight API integration)

## Architecture

```
UI Layer (Compose Screens)
    ↓
Domain Layer (Use Cases, AisleMatcher, Models)
    ↓
Data Layer (Room DB, Repositories, Seed Data)
```

### Core Algorithm: Three-Tier Aisle Matching

1. **Exact match** — normalized item name looked up in product database
2. **Category keyword match** — item matched against category keyword lists (e.g., "cheese" → Dairy)
3. **Heuristic inference** — rule-based category guessing (e.g., contains "frozen" → Frozen Foods)
4. **Unknown** — item placed at end; user can manually assign aisle (saved for next time)

## Roadmap

- **Phase 1 (Current)**: Manual list entry, aisle sorting, clipboard import
- **Phase 2**: Skylight integration (automated grocery list import)
- **Phase 3**: Multi-store support (multiple Meijer locations)
