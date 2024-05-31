#!/bin/bash
input_video="$1"
output_dir=src/main/resources/assets/rickroll/textures
temp_dir=$(mktemp -d)
if [ -z "$input_video" ]; then
  echo "Usage: $0 <input_video>"
  exit 1
fi

mkdir -p "$output_dir"
ffmpeg -i "$input_video" -vf "fps=20" "$temp_dir/frame%04d.png"

for frame in "$temp_dir"/frame*.png; do
  base_name=$(basename "$frame")
  convert "$frame" -gravity center -crop 1152x1152+0+0 +repage "$output_dir/$base_name"
done

rm -rf "$temp_dir"
echo "Frames extracted and cropped successfully."
