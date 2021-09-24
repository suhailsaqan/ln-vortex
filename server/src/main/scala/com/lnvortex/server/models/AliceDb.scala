package com.lnvortex.server.models

import org.bitcoins.core.hd._
import org.bitcoins.core.protocol.transaction.TransactionOutput
import org.bitcoins.crypto._

case class AliceDb(
    peerId: Sha256Digest,
    roundId: DoubleSha256Digest,
    purpose: HDPurpose,
    coin: HDCoinType,
    accountIdx: Int,
    chain: HDChainType,
    nonceIndex: Int,
    nonce: SchnorrNonce,
    numInputs: Int,
    blindedOutputOpt: Option[FieldElement],
    changeOutputOpt: Option[TransactionOutput],
    blindOutputSigOpt: Option[FieldElement]
) {

  val noncePath: BIP32Path = {
    val coin = HDCoin(purpose, this.coin)
    val account = HDAccount(coin, accountIdx)
    val chain = HDChain(this.chain, account)
    val path = HDAddress(chain, nonceIndex).path
    // We need to make sure this is hardened for security
    val hardened = path.map(_.copy(hardened = true))
    BIP32Path(hardened)
  }

  def setOutputValues(
      numInputs: Int,
      blindedOutput: FieldElement,
      changeOutput: TransactionOutput,
      blindOutputSig: FieldElement): AliceDb = {
    copy(numInputs = numInputs,
         blindedOutputOpt = Some(blindedOutput),
         changeOutputOpt = Some(changeOutput),
         blindOutputSigOpt = Some(blindOutputSig))
  }
}

object AliceDbs {

  def newAlice(
      peerId: Sha256Digest,
      roundId: DoubleSha256Digest,
      noncePath: BIP32Path,
      nonce: SchnorrNonce): AliceDb = {
    require(noncePath.size == 5,
            s"nonce path must have a size of 5, got ${noncePath.size}")
    val purpose = noncePath.path.head
    val _ :+ coin :+ account :+ chain :+ address = noncePath.path

    AliceDb(
      peerId = peerId,
      roundId = roundId,
      purpose = HDPurpose(purpose.index),
      coin = HDCoinType.fromInt(coin.index),
      accountIdx = account.index,
      chain = HDChainType.fromInt(chain.index),
      nonceIndex = address.index,
      nonce = nonce,
      numInputs = 0,
      blindedOutputOpt = None,
      changeOutputOpt = None,
      blindOutputSigOpt = None
    )
  }
}
