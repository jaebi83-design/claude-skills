# Claude Skills Collection

A curated collection of Claude Code skills for various use cases. This repository serves as a personal library of reusable skills that can be easily installed across different computers.

## What Are Claude Skills?

Claude Code skills are reusable instruction sets that extend Claude's capabilities for specific tasks. Each skill contains structured instructions that Claude follows when the skill is active.

## Repository Structure

```
claude-skills/
├── README.md              # This file
└── skills/                # All skills organized here
    ├── example-skill/
    │   └── SKILL.md
    ├── my-custom-skill/
    │   └── SKILL.md
    └── another-skill/
        └── SKILL.md
```

## Installation Instructions

### Install All Skills

To install all skills from this repository to your local Claude Code environment:

```bash
# Clone the repository
git clone https://github.com/jaebi83-design/claude-skills.git

# Copy all skills to your personal Claude skills directory
cp -r claude-skills/skills/* ~/.claude/skills/

# Verify installation
ls ~/.claude/skills/
```

### Install Individual Skills

To install only specific skills:

```bash
# Clone the repository
git clone https://github.com/jaebi83-design/claude-skills.git

# Copy only the skill you need
cp -r claude-skills/skills/example-skill ~/.claude/skills/

# Or copy multiple specific skills
cp -r claude-skills/skills/{example-skill,my-custom-skill} ~/.claude/skills/
```

### Install to a Specific Project

To install skills for a specific project only (not globally):

```bash
# Navigate to your project directory
cd /path/to/your/project

# Create project skills directory if it doesn't exist
mkdir -p .claude/skills

# Copy skills to project
cp -r /path/to/claude-skills/skills/* .claude/skills/

# Commit to version control
git add .claude/skills/
git commit -m "Add Claude skills to project"
```

## Usage

Once installed, skills can be used in two ways:

### 1. Manual Invocation
```bash
/example-skill
/my-custom-skill [arguments]
```

### 2. Auto-Detection
Claude will automatically activate skills based on context when their description matches the current task.

## Available Skills

### From 2389 Research Collection

#### binary-re
Binary reverse engineering with hypothesis-driven analysis. Routes to specialized sub-skills for triage, static analysis, dynamic analysis, synthesis, and tool setup. Includes human-in-the-loop safeguards.

#### building-multiagent-systems
Multi-agent architecture patterns with four-layer stack (Reasoning → Orchestration → Tool Bus → Deterministic Adapters). Seven coordination patterns including Fan-Out/Fan-In, Pipeline, Recursive Delegation, and MAKER.

#### ceo-personal-os
Executive reflection system combining 11 strategic frameworks with coaching-style interview scripts. Includes daily, weekly, quarterly, and annual review cadences. Not a task manager—focused on clarity over productivity theater.

#### css-development
CSS development using Tailwind and semantic patterns. Routes to sub-skills for creating components, validation, and refactoring. Dark mode by default, composition-first approach.

#### documentation-audit
Systematically verifies documentation claims against actual codebase. Two-pass verification process catches dead links, incorrect configs, and behavioral mismatches before releases.

#### firebase-development
Complete Firebase development workflows. Routes to sub-skills for project setup, adding features, debugging, and validation. Based on production patterns with TypeScript, vitest, and biome.

#### fresh-eyes-review
Pre-commit quality gate that catches bugs tests miss. Seven-step process covering security, logic errors, business rules, and performance. Takes 2-5 minutes, mandatory before commits.

#### gtm-partner
Go-to-market strategy consultant that recommends channels, validates strategy, and generates assets. Includes naming, pricing analysis, landing page, and channel-specific content. Delivers single HTML webpage with complete GTM plan.

#### landing-page-design
High-converting landing pages with anti-AI-slop design patterns. Vibe discovery process creates distinctive aesthetics. Covers hero sections, features, social proof, pricing, and CTAs.

#### product-launcher
Generates coordinated launch materials: subscriber email, CEO blog post, and tweet thread. Uses established voice profiles for 2389 audience with Slack integration for team review.

#### remote-system-maintenance
Linux system diagnostics and maintenance for Ubuntu/Debian. Seven-stage cleanup sequence can recover 2+ GB. Includes diagnostics, log review, and package assessment.

#### scenario-testing
Real-dependency testing with zero mocks. "NO FEATURE IS VALIDATED UNTIL A SCENARIO PASSES WITH REAL DEPENDENCIES." Tests in `.scratch/` directory, extracts patterns to `scenarios.jsonl`.

#### terminal-title
Automatically updates terminal title with emoji, project name, and current topic. Triggers at session start and topic changes. Cross-platform support (bash/PowerShell).

#### test-kitchen
Parallel exploration framework with two gate skills: Omakase-off (entry) for design exploration and Cookoff (exit) for implementation comparison. Enables comparing multiple approaches simultaneously.

#### worldview-synthesis
Personal philosophy documentation through systematic interrogation. Six-phase workflow covering 19 life domains. Generates outputs from 100-word mission to 2000-word narrative. Embraces tensions rather than forcing resolution.

#### xtool
Cross-platform Xcode-free iOS development using SwiftPM on Linux, Windows, and macOS. Covers project setup, app extensions (widgets, share, etc.), and deployment.

### Template Skills

#### example-skill
A template skill demonstrating the basic structure. Use this as a starting point for creating new skills.

#### my-custom-skill
A custom skill placeholder. Customize this for your specific use case.

#### another-skill
Another skill placeholder ready for customization.

## Adding New Skills

To add a new skill to this collection:

1. Create a new directory in `skills/` with your skill name
2. Create a `SKILL.md` file inside with proper frontmatter and instructions
3. Commit and push to this repository

## Updating Skills on Another Computer

To update your skills when changes are made to this repository:

```bash
# Navigate to where you cloned this repo
cd claude-skills

# Pull latest changes
git pull origin main

# Re-copy updated skills (this will overwrite existing)
cp -r skills/* ~/.claude/skills/
```

## Uninstalling Skills

To remove a skill:

```bash
# Remove from personal skills
rm -rf ~/.claude/skills/skill-name

# Or remove from project skills
rm -rf /path/to/project/.claude/skills/skill-name
```

## Resources

- [Claude Code Documentation](https://code.claude.com/docs)
- [Agent Skills Specification](https://agentskills.io)
- [Official Anthropic Skills](https://github.com/anthropics/skills)
- [Awesome Claude Skills](https://github.com/travisvn/awesome-claude-skills)

## License

These skills are available for personal use. Modify and adapt as needed for your workflows.
