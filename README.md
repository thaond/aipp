# AIPP - Java Port

Java conversion of the AIP Parser (originally Ruby gem).

## Overview

This is a Java implementation of AIPP - Parser for aeronautical information available online.

This application includes executables to download and parse aeronautical information (HTML, PDF, XLSX, ODS and CSV), then build and export as [AIXM](https://github.com/svoop/aixm) or [OFMX](https://github.com/svoop/ofmx).

## Project Structure

```
src/main/java/com/aipp/
├── AIPP.java                 # Main environment and context holder
├── cli/                      # Command-line interfaces
│   ├── Aip2Aixm.java        # AIP to AIXM converter
│   └── Aip2Ofmx.java        # AIP to OFMX converter
├── downloader/              # Document downloaders
│   ├── Downloader.java      # Abstract base
│   ├── FileDownloader.java  # Local file handling
│   ├── HttpDownloader.java  # HTTP/HTTPS handling
│   └── GraphQLDownloader.java # GraphQL API handling
├── environment/             # Runtime environment objects
│   ├── Cache.java          # Transient object cache
│   ├── Borders.java        # Border definitions
│   ├── Fixtures.java       # Static YAML fixtures
│   ├── Options.java        # Command-line options
│   └── Config.java         # Configuration management
├── parser/                  # Parser framework
│   └── Parser.java          # Base parser class
└── regions/                 # Region-specific implementations
    ├── lf/                 # France (LF) parsers
    └── ls/                 # Switzerland (LS) parsers
```

## Building

```bash
mvn clean install
```

## Usage

### AIP to AIXM

```bash
java -jar target/aipp.jar aip2aixm -r LF
```

### AIP to OFMX

```bash
java -jar target/aipp.jar aip2ofmx -r LF
```

### Options

- `-r, --region REGION` - Aeronautical region (e.g., LF, LS)
- `-s, --scope SCOPE` - Scope: AIP (default), NOTAM, or SHOOT
- `-S, --section SECTION` - Specific section to parse
- `--storage DIR` - Storage directory (default: ~/.aipp)
- `-l, --list` - List available regions and sections
- `-v, --verbose` - Verbose output
- `--debug-on-error` - Open debugger on error
- `-h, --help` - Show help message

## Dependencies

- **HTML/XML Parsing**: JSoup 1.15.3
- **Excel/ODS**: Apache POI 5.2.3
- **PDF**: Apache PDFBox 2.0.28
- **HTTP Client**: OkHttp 4.10.0
- **GraphQL**: GraphQL Java 20.0
- **JSON/YAML**: Jackson 2.15.2
- **Utilities**: Guava 31.1, Apache Commons Lang 3.12.0
- **Logging**: Log4j 2.20.0

## Development

### Running Tests

```bash
mvn test
```

### Running with Debug

```bash
mvn exec:java@aip2aixm -Dexec.args="-r LF"
```

## Storage

AIPP uses a storage directory for configuration, caching, and previous run results. Default location is `~/.aipp`, but you can specify a different directory with `--storage`.

## Conversion Status

This is an active Java conversion from the original Ruby gem. Current focus:

- [x] Maven project structure
- [x] Core Parser framework
- [x] Downloader abstractions
- [x] Environment management
- [x] CLI entry points
- [ ] Region-specific parsers (LF, LS)
- [ ] AIXM output generation
- [ ] OFMX output generation
- [ ] Full test coverage

## References

- [Original Ruby Gem](https://github.com/svoop/aipp)
- [AIXM](https://github.com/svoop/aixm)
- [OFMX](https://github.com/svoop/ofmx)
- [Open FlightMaps](https://openflightmaps.org)

## License

MIT License
