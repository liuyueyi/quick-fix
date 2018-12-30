package com.git.hui.fix.api.modal;

import lombok.Getter;

public final class ImmutablePair<L, R> {
    @Getter
    private final L left;
    @Getter
    private final R right;

    private ImmutablePair(final L l, final R r) {
        this.left = l;
        this.right = r;
    }

    public static <L, R> ImmutablePair<L, R> of(L left, R right) {
        return new ImmutablePair<>(left, right);
    }
}