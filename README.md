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

### example-skill
A template skill demonstrating the basic structure. Use this as a starting point for creating new skills.

### my-custom-skill
A custom skill placeholder. Customize this for your specific use case.

### another-skill
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
