import { LeftClick, mc, RightClick, swaptoslot } from "../Utils";

// swap from wither blade to astraea for more damage with ferocity
const claymoreswordswap = (event) => {
            if (Client.currentGui.get() === null) {
                    if (Player.getHeldItem() !== null && Player.getHeldItem().getName().removeFormatting().toLowerCase().includes("wool")) {
                        for (let i = 0; i < 9; i++) {
                            if (Player.getInventory().getStackInSlot(i) !== null && Player.getInventory().getStackInSlot(i).getName().removeFormatting().toLowerCase().includes("stone")) {
                                Client.sendPacket(new swaptoslot(i));
                                RightClick.invoke(mc)
                                Client.sendPacket(new swaptoslot(Player.getInventory().getInventory().field_70461_c));
                                break;
                    
                }
            }
        }
    }
}

register("clicked", (event) => {
    if (Client.currentGui.get() === null) {
        if (Player.getHeldItem() !== null && Player.getHeldItem().getName().removeFormatting().toLowerCase().includes("wool")) {
            for (let i = 0; i < 9; i++) {
                if (Player.getInventory().getStackInSlot(i) !== null && Player.getInventory().getStackInSlot(i).getName().removeFormatting().toLowerCase().includes("stone")) {
                    Client.sendPacket(new swaptoslot(i));
                    RightClick.invoke(mc)
                    Client.sendPacket(new swaptoslot(Player.getInventory().getInventory().field_70461_c));
                    break;
        
    }
}
}
}
});