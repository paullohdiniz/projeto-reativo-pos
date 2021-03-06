package br.com.projetoreativo.pos.buscamelhorcaminho.model;

import java.util.Date;
import java.util.List;

import br.com.projetoreativo.pos.buscamelhorcaminho.util.StringUtil;

public class BlockBusiness {

//	private String previousHash;
//	private String data;
//	public String hash;
//	private int nonce;

	public BlockBusiness() {

	}

	// Calculate new hash based on blocks contents
	public String calculateHash(Block block, boolean isValida) {
		
		if(!isValida)
			block.setDateTime(new Date().getTime());
		
		String calculatedhash = StringUtil
				.applySha256(block.getPreviousHash() + Long.toString(block.getDateTime()) + block.getData());
		return calculatedhash;
	}

	public void mineBlock(Block block, int difficulty) {
		
		String target = StringUtil.getDificultyString(difficulty); 
		
		String hash = block.getHash();
		
		while (!hash.substring(0, difficulty).equals(target)) {
			//nonce++;
			hash = calculateHash(block, false);
		}
		
		block.setHash(hash);
		System.out.println("Block Mined!!! : " + hash);
	}

	public Block save(Block block, final int difficulty) {

		block.setHash(calculateHash(block, false)); 
		mineBlock(block, difficulty);

		return block;
	}

	
	public Boolean isChainValid(List<Block> blockchain, int difficulty) {
		Block currentBlock; 
		Block previousBlock;
		String hashTarget = new String(new char[difficulty]).replace('\0', '0');
		
		//loop through blockchain to check hashes:
		for(int i=1; i < blockchain.size(); i++) {
			currentBlock = blockchain.get(i);
			previousBlock = blockchain.get(i-1);
			//compare previous hash and registered previous hash
			if(!previousBlock.getHash().equals(currentBlock.getPreviousHash()) ) {
				System.out.println("Previous Hashes not equal");
				return false;
			}
			//compare registered hash and calculated hash:
			if(!currentBlock.getHash().equals(calculateHash(currentBlock, true)) ){
				System.out.println("Current Hashes not equal");			
				return false;
			}
			//check if hash is solved
			if(!currentBlock.getHash().substring( 0, difficulty).equals(hashTarget)) {
				System.out.println("This block hasn't been mined");
				return false;
			}
			System.out.println("Validado Bloco: " +previousBlock.getId() + "/"+ previousBlock.getData());
			
		}
		
		return true;
	}
}
