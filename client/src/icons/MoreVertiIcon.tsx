import * as React from "react"

interface Props {
  color?: string
  width?: number
  height?: number
}

export default function MoreVertiIcon({ color = "#3DC7BE", width = 24, height = 24 }: Props) {
  return (
    <svg width={width} height={height} viewBox="0 0 24 24" fill={color} version="1.1" xmlns="http://www.w3.org/2000/svg">
      <path d="M0 0h24v24H0z" fill="none" />
      <path
        // tslint:disable-next-line:max-line-length
        d="M12 8c1.1 0 2-.9 2-2s-.9-2-2-2-2 .9-2 2 .9 2 2 2zm0 2c-1.1 0-2 .9-2 2s.9 2 2 2 2-.9 2-2-.9-2-2-2zm0 6c-1.1 0-2 .9-2 2s.9 2 2 2 2-.9 2-2-.9-2-2-2z"
      />
    </svg>
  )
}
