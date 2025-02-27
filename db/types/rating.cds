namespace com.example.bookshop;

type Rating : Integer @assert.range: [
  0,
  5
]
