from typing import NamedTuple, Literal, Tuple

#Приймає тип: рядок чи стовпець для позначень
#Та позиція х,у (початок з 0 0)

class GameAction(NamedTuple):
    action_type: Literal["row", "col"]
    position: Tuple[int, int]
