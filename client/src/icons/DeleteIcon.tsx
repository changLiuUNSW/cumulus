import * as React from "react"

interface Props {
  color?: string
  width?: number
  height?: number
}

export default function DeleteIcon({ color = "#3DC7BE", width = 20, height = 20 }: Props) {
  return (
    <svg fill={color} width={width} height={height} viewBox="0 0 24 24">
      <path d="M6 19c0 1.1.9 2 2 2h8c1.1 0 2-.9 2-2V7H6v12zM19 4h-3.5l-1-1h-5l-1 1H5v2h14V4z" />
      <path d="M0 0h24v24H0z" fill="none" />
    </svg>
  )
}
