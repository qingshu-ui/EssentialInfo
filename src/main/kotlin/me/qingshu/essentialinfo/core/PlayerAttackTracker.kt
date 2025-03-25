package me.qingshu.essentialinfo.core

import net.minecraft.entity.LivingEntity
import java.util.*

object PlayerAttackTracker {

    private var lastAttackedEntity: UUID? = null
    private var lastAttackTime: Long = -1

    @JvmStatic
    fun recordAttack(entity: LivingEntity) {
        lastAttackedEntity = entity.uuid
        lastAttackTime = System.currentTimeMillis()
    }

    @JvmStatic
    fun isLastAttacked(entity: LivingEntity): Boolean {
        if (lastAttackedEntity == null) return false
        return entity.uuid.equals(lastAttackedEntity)
               && System.currentTimeMillis() - lastAttackTime < 1000
    }
}