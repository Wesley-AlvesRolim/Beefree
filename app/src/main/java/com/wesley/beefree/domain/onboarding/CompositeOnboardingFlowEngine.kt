package com.wesley.beefree.domain.onboarding

import com.wesley.beefree.domain.onboarding.ports.OnboardingFlowEngine

class CompositeOnboardingFlowEngine(
    private val root: OnboardingNode,
) : OnboardingFlowEngine {
    private val history = mutableListOf<ResolvedSequence>()
    private var cursor = 0

    init {
        resolveCurrent()
    }

    private data class ResolvedSequence(
        val steps: List<OnboardingStep>,
        val branch: OnboardingBranch?,
        val remaining: List<OnboardingNode>,
        val startIndex: Int,
    ) {
        val lastIndex = startIndex + steps.size - 1
    }

    override val currentStep: OnboardingStep
        get() {
            val seq = history.find { cursor in it.startIndex..it.lastIndex } ?: history.last()
            return seq.steps[cursor - seq.startIndex]
        }

    override val isFirst: Boolean get() = cursor == 0

    override val isLast: Boolean
        get() {
            val lastSeq = history.last()
            return cursor == lastSeq.lastIndex && lastSeq.branch == null
        }

    override fun next(answers: OnboardingAnswers) {
        val currentSeq = history.find { cursor in it.startIndex..it.lastIndex }!!

        if (cursor < currentSeq.lastIndex) {
            cursor++
        } else if (currentSeq.branch != null) {
            val resolved = currentSeq.branch.resolve(answers)
            val seqIndex = history.indexOf(currentSeq)
            while (history.size > seqIndex + 1) history.removeAt(history.size - 1)

            resolveNodes(listOf(resolved) + currentSeq.remaining, cursor + 1)
            cursor++
        }
    }

    override fun previous() {
        if (cursor > 0) cursor--
    }

    private fun resolveCurrent() {
        history.clear()
        resolveNodes(listOf(root), 0)
    }

    private fun resolveNodes(
        nodes: List<OnboardingNode>,
        startIndex: Int,
    ) {
        val steps = mutableListOf<OnboardingStep>()
        var i = 0
        while (i < nodes.size) {
            when (val node = nodes[i]) {
                is OnboardingStep -> {
                    steps.add(node)
                    i++
                }
                is OnboardingSequence -> {
                    resolveNodes(node.children + nodes.drop(i + 1), startIndex + steps.size)
                    return
                }
                is OnboardingBranch -> {
                    history.add(ResolvedSequence(steps, node, nodes.drop(i + 1), startIndex))
                    return
                }
            }
        }
        if (steps.isNotEmpty()) {
            history.add(ResolvedSequence(steps, null, emptyList(), startIndex))
        }
    }
}
