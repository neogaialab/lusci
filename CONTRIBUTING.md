# Contributing Guide

Thank you for considering contributing to Lusci! We welcome contributions from the community to help improve the project and make it even better. This guide outlines the various ways you can contribute.

1. **Reporting Bugs:** If you encounter any bugs or issues with the project, please [open an issue](https://github.com/neogaialab/lusci/issues) on GitHub. Include detailed information about the bug and steps to reproduce it.
2. **Feature Requests:** If you have ideas for new features or improvements, feel free to [open an issue](https://github.com/neogaialab/lusci/issues) to discuss them. We value your feedback and ideas.
3. **Code Contributions:** If you're a developer and want to contribute code to the project, you can fork the repository, make your changes, and submit a pull request. Please follow our coding standards and guidelines.
4. **Documentation Improvements:** Help us improve the project's documentation by fixing typos, adding examples, or clarifying instructions. You can edit the documentation directly on GitHub.

## Development

### Tools

We use the following tools for development:

- [OpenJDK](https://openjdk.org/install/)
- [Gradle](https://gradle.org/install/)
- [Git](https://git-scm.com/downloads)
- [Vscode](https://code.visualstudio.com/download) (optional)
  - [Language Support for Java(TM) by Red Hat for Vscode](https://marketplace.visualstudio.com/items?itemName=redhat.java) (optional)
  - [IntelliCode for Vscode](https://marketplace.visualstudio.com/items?itemName=VisualStudioExptTeam.vscodeintellicode) (optional)

### Installation

```bash
git clone https://github.com/neogaialab/lusci
cd lusci
```

### Committing

- [Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/)
  - [Angular Commit Convention](https://github.com/angular/angular/blob/main/CONTRIBUTING.md#commit)

### Releasing

- [Semver](https://semver.org/)
- [Keep a Changelog](https://keepachangelog.com/en/1.1.0/)

### Scripts

- `gradle run`: Start.
- `gradle shadowJar`: Build JAR for production.
- `docker compose up`: Run JAR using Docker.
- `docker compose down`: Stop JAR using Docker.
- `gradle release`: Release new version.
