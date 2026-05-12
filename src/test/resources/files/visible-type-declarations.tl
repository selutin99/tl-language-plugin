-- an enum: a set of accepted strings
local enum State
   "open"
   "closed"
end

-- a record: a table with a known set of fields
local record Point
   x: number
   y: number
end

-- an interface: an abstract record type
local interface Character
   sprite: Image
   position: Point
   kind: string
end

-- records can implement interfaces, using a type-identifying `where` clause
local record Spaceship
   is Character
   where self.kind == "spaceship"

   weapon: Weapons
end

-- a record can also declare an array interface, making it double as a record and an array
local record TreeNode<T>
   is {TreeNode<T>}

   item: T
end

-- a userdata record: a record which is implemented as a userdata
local record File
   is userdata

   status: function(): State
   close: function(File): boolean, string
end

global enum GlobalState
   "ready"
end

global record GlobalPoint
   x: number
end

global interface GlobalCharacter
   kind: string
end
