# Video Decomposer

Video Decomposer is a program that takes a long video and chops it up into smaller videos. It looks for words that appear on the screen (usually at the bottom) and uses those words to figure out where one section ends and the next one begins. Each smaller video gets named after the words it found on screen.

For example, if you have one big video of aikido techniques, and each technique has its name shown at the bottom of the screen, this program will automatically split that big video into separate little videos — one for each technique — and name each file after the technique.

---

## What You Need to Install First

Before you can use this program, you need three things installed on your computer:

### 1. Python

Python is the programming language this program is written in.

- Go to https://www.python.org/downloads/
- Download the latest version for Windows
- Run the installer
- **IMPORTANT:** Check the box that says "Add Python to PATH" before you click Install

To check if it worked, open a Command Prompt and type:
```
python --version
```
You should see a version number like `Python 3.12.x`.

### 2. FFmpeg

FFmpeg is a tool that does the actual video cutting.

- Go to https://ffmpeg.org/download.html
- Download the Windows build (look for "Windows builds" from gyan.dev)
- Extract the zip file somewhere (like `C:\ffmpeg`)
- Add the `bin` folder to your PATH:
  - Press `Win + R`, type `sysdm.cpl`, hit Enter
  - Go to the **Advanced** tab, click **Environment Variables**
  - Under "System variables", find **Path**, click **Edit**
  - Click **New** and add the path to the `bin` folder (like `C:\ffmpeg\bin`)
  - Click OK on all dialogs

To check if it worked, open a **new** Command Prompt and type:
```
ffmpeg -version
```
You should see version info.

### 3. Tesseract OCR

Tesseract is what reads the words from the video frames.

- Go to https://github.com/UB-Mannheim/tesseract/wiki
- Download the 64-bit Windows installer (the `.exe` file)
- Run the installer with all the default settings
- Add it to your PATH:
  - Press `Win + R`, type `sysdm.cpl`, hit Enter
  - Go to the **Advanced** tab, click **Environment Variables**
  - Under "System variables", find **Path**, click **Edit**
  - Click **New** and add: `C:\Program Files\Tesseract-OCR`
  - Click OK on all dialogs

To check if it worked, open a **new** Command Prompt and type:
```
tesseract --version
```
You should see version info.

---

## How to Set Up the Program

### Step 1: Get the code

Open a Command Prompt and type:
```
git clone https://github.com/jaebi83-design/claude-skills.git
cd claude-skills
git checkout claude/create-video-decomposer-Z6Iwy
cd video-decomposer
```

### Step 2: Install the Python packages

```
pip install -r requirements.txt
```

If `pip` doesn't work, try using the full path to your Python:
```
"C:\Users\YourName\AppData\Local\Programs\Python\Python312\python.exe" -m pip install -r requirements.txt
```
(Replace `YourName` with your actual Windows username and `Python312` with your version.)

### Step 3: Set up the database

```
python -c "from app.database import init_db; init_db()"
```

That's it! You're ready to go.

---

## How to Use the Program

### Step 1: Start the server

Open a Command Prompt, go to the video-decomposer folder, and run:
```
cd C:\Users\YourName\claude-skills\video-decomposer
python -m uvicorn app.main:app --host 0.0.0.0 --port 8000
```

You should see something like:
```
INFO:     Uvicorn running on http://0.0.0.0:8000
```

**Leave this window open.** The server runs in this window. If you close it, the program stops.

### Step 2: Open the program in your browser

Open your web browser (Chrome, Edge, etc.) and go to:
```
http://localhost:8000
```

You will see the **Dashboard** page. It will be empty at first.

### Step 3: Upload a video

- Click **Upload** in the top menu
- Click **"Select a video file"** and pick your video file from your computer
- Leave the **Frame sample interval** at 1.0 (this means it checks for text once every second)
- Click **"Upload & Decompose"**

A progress bar will show while the video uploads. Once it's done, it will automatically start processing.

### Step 4: Wait for processing

You will be taken to a **Job** page that shows:
- **Status: processing** with a spinning icon
- The page refreshes itself every few seconds

How long it takes depends on how long your video is:
- A 30-second video takes a few seconds
- A 5-minute video takes about a minute
- A 20-minute video might take several minutes

### Step 5: See your results

When processing is done, the page will show:
- **Status: complete**
- A grid of **segment cards** — each one is a separate clip that was found
- Each card shows:
  - A thumbnail image from that clip
  - The **label** (the words that were on screen)
  - The **time range** (where in the original video this clip came from)
  - A **"View Clip"** button

### Step 6: Watch or find your clips

- Click **"View Clip"** on any segment to watch it in the browser
- The actual video files are saved on your computer in:
  ```
  C:\Users\YourName\claude-skills\video-decomposer\data\clips\
  ```
- Each clip is named after its label, like `YOKOMEN_UCHI_HIJI_OSAE_ICHI.mp4`
- You can copy these files to wherever you want

---

## Where Files Are Stored

| What | Location |
|------|----------|
| Your uploaded videos | `video-decomposer\data\originals\` |
| The split clips | `video-decomposer\data\clips\` |
| Thumbnail images | `video-decomposer\data\thumbnails\` |
| The database | `video-decomposer\data\decomposer.db` |

---

## Tips

- **If the program finds too many segments:** The text detection might be picking up noise. Try uploading again — it only looks at the bottom 20% of the screen for text.
- **If the program finds too few segments:** The words on your video might be too small or blurry for the OCR to read.
- **To start fresh:** Delete the database file (`data\decomposer.db`) and restart the server. Everything resets.
- **To stop the server:** Go to the Command Prompt window where it's running and press `Ctrl+C`.
- **If something says "not recognized":** You probably need to close your Command Prompt and open a new one after installing something, so it picks up the new PATH settings.

---

## Troubleshooting

| Problem | Solution |
|---------|----------|
| `'python' is not recognized` | Use the full path: `"C:\Users\YourName\AppData\Local\Programs\Python\Python312\python.exe"` |
| `'ffmpeg' is not recognized` | Add FFmpeg's `bin` folder to your PATH (see install steps above) |
| `tesseract is not installed or not in your PATH` | Install Tesseract and add it to PATH (see install steps above) |
| `[WinError 2] The system cannot find the file specified` | FFmpeg is not in your PATH |
| `address already in use` | The server is already running. Close all Command Prompt windows and try again |
| `Internal Server Error` | Delete `data\decomposer.db` and restart the server |
| Browser says "site can't be reached" | Make sure the server is running in a Command Prompt window |

---

## What Each File Does

```
video-decomposer/
├── setup.sh                    # Setup script (for Mac/Linux)
├── requirements.txt            # List of Python packages needed
├── README.md                   # This file
├── app/
│   ├── config.py               # Settings (where files go, OCR settings)
│   ├── database.py             # Stores info about videos, jobs, segments
│   ├── models.py               # Data structures used by the program
│   ├── main.py                 # Starts the web server
│   ├── video_processor.py      # Uses FFmpeg to cut videos and make thumbnails
│   ├── text_detector.py        # Uses Tesseract to read words from video frames
│   ├── decomposer.py           # The brain — coordinates detecting and splitting
│   ├── routes/
│   │   ├── api.py              # Handles upload and decompose requests
│   │   ├── pages.py            # Serves the web pages you see in your browser
│   │   └── video.py            # Streams video files to your browser
│   ├── static/
│   │   ├── style.css           # Makes the web pages look nice
│   │   └── app.js              # Makes buttons and uploads work
│   └── templates/
│       ├── base.html           # The page layout (header, nav bar)
│       ├── index.html          # Dashboard page (list of jobs and videos)
│       ├── upload.html         # Upload page (pick a file)
│       ├── job.html            # Job detail page (see segments found)
│       └── clip.html           # Clip page (watch one segment)
└── data/                       # Created when you run the program
    ├── originals/              # Your uploaded videos
    ├── clips/                  # The split video clips
    ├── thumbnails/             # Preview images for each clip
    └── decomposer.db           # Database tracking everything
```
