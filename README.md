# HyperBitT, HyperBitBit64 and HyperTwoBits

This repository will serve as reference code for the paper "Bit-array-based alternatives to HyperLogLog" by Svante Janson, Jérémie Lumbroso and Bob Sedgewick.

The paper itself contains reference implementations in Java, but in this repository we propose a fully runnable version. We rely on the [`randomhash` Java package](https://github.com/jlumbroso/java-random-hash) (for which there [exists an exact Python equivalent](https://github.com/jlumbroso/python-random-hash)) that implements affine combinations of as CRC32 hash, with Mersenne Twister generated parameters. These family of hash functions have been shown to have nice distribution and independence properties, and they behave in practice as though they are sufficiently decorrelated, even if they are by construction.

We currently have a reference implementation `HyperBit64.java` which is deprecated, but we will update this repository with our final code soon.

## Other Implementations

- [Heinz N. Gies](https://github.com/Licenser): [https://github.com/axiomhq/hypertwobits/](https://github.com/axiomhq/hypertwobits/)
